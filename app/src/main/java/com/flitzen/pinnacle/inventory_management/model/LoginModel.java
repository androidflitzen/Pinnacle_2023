package com.flitzen.pinnacle.inventory_management.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("USERS_ROLES_ID")
        @Expose
        private String uSERSROLESID;
        @SerializedName("NAME")
        @Expose
        private String nAME;
        @SerializedName("EMAIL")
        @Expose
        private String eMAIL;
        @SerializedName("PASSWORD")
        @Expose
        private String pASSWORD;
        @SerializedName("PHONE_NUMBER")
        @Expose
        private String pHONENUMBER;
        @SerializedName("USER_ROLES")
        @Expose
        private String uSERROLES;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getUSERSROLESID() {
            return uSERSROLESID;
        }

        public void setUSERSROLESID(String uSERSROLESID) {
            this.uSERSROLESID = uSERSROLESID;
        }

        public String getNAME() {
            return nAME;
        }

        public void setNAME(String nAME) {
            this.nAME = nAME;
        }

        public String getEMAIL() {
            return eMAIL;
        }

        public void setEMAIL(String eMAIL) {
            this.eMAIL = eMAIL;
        }

        public String getPASSWORD() {
            return pASSWORD;
        }

        public void setPASSWORD(String pASSWORD) {
            this.pASSWORD = pASSWORD;
        }

        public String getPHONENUMBER() {
            return pHONENUMBER;
        }

        public void setPHONENUMBER(String pHONENUMBER) {
            this.pHONENUMBER = pHONENUMBER;
        }

        public String getUSERROLES() {
            return uSERROLES;
        }

        public void setUSERROLES(String uSERROLES) {
            this.uSERROLES = uSERROLES;
        }
    }
}

