package com.thetatechno.fluidadmin.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.Constants;

public class MainViewModel extends ViewModel {
    final static private String TAG = MainViewModel.class.getSimpleName();
    private StaffRepository staffRepository = new StaffRepository();
    private CodeRepository codeRepository = new CodeRepository();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private MutableLiveData<String> deletedStaffMessageLiveData = new MutableLiveData<>();
    private MutableLiveData<String> deletedCodeMessageLiveData = new MutableLiveData<>();
    private  MutableLiveData<String> deletedFacilityMessageLiveData = new MutableLiveData<>();
    private String messageForStaff = "";
    private String messageForCode = "";
    private  String messageForSFacility = "";

    public MutableLiveData<String> deleteAgentOrProvider(final Staff staff) {
        staffRepository.deleteStaff(staff.getStaffId(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteAgentOrProvider: delete state " + b);
                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForStaff = "Delete " + staff.getFirstName() + " successfully";
                }
                else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)){
                    messageForStaff = "Cannot delete, Provider has a schedule.";

                }else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForStaff = "Failed to delete.";

                }
                deletedStaffMessageLiveData.setValue(messageForStaff);

            }
        });
        return deletedStaffMessageLiveData;
    }

    public MutableLiveData<String> deleteCode(final Code code) {
        codeRepository.deleteCode(code.getCodeType(), code.getCode(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteCode: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForCode = "Delete code " + code.getCode() + " successfully ";
//                    codeRepository.getAllCodes(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                    deletedCodeMessageLiveData.setValue(messageForCode);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForCode = "Failed to delete code " + code.getCode();
                    deletedCodeMessageLiveData.setValue(messageForCode);
                }

            }
        });
        return deletedCodeMessageLiveData;
    }

    public MutableLiveData<String> deleteFacility(final Facility facility) {
        facilityRepository.deleteFacility(facility.getId(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteFacility: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForSFacility = "Delete facility " + facility.getId()+ " successfully";
                    deletedFacilityMessageLiveData.setValue(messageForSFacility);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForSFacility = "Failed to delete facility " + facility.getId();
                    deletedFacilityMessageLiveData.setValue(messageForSFacility);
                }

            }
        });
        return deletedFacilityMessageLiveData;
    }

    public void linkToFacility(String staffId, FacilityCodes facilityCodes, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {
        facilityRepository.linkToFacility(staffId, facilityCodes, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                onDataChangedCallBackListener.onResponse(b);
            }
        });
    }

}
