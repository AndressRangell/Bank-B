package com.bcp.rest.giros.cobro_nacional.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqListProducts extends JsonUtil {

        //REQUEST
        private static final String REMITTERCARD = "remitterCard";
        private static final String BENEFICIARYCARD = "beneficiaryCard";
        private static final String TRACK2 = "track2";

        private String reqBeneficiaryCard;
        private String reqTrack2;

        public String getReqBeneficiaryCard() {
            return reqBeneficiaryCard;
        }

        public void setReqBeneficiaryCard(String reqBeneficiaryCard) {
            this.reqBeneficiaryCard = reqBeneficiaryCard;
        }

        public String getReqTrack2() {
            return reqTrack2;
        }

        public void setReqTrack2(String reqTrack2) {
            this.reqTrack2 = reqTrack2;
        }

        public JSONObject buildsObjectJSON(int typeTrans){

            JSONObject request = new JSONObject();
            JSONObject jsonBeneficiaryCard = new JSONObject();

            try {
                jsonBeneficiaryCard.put(TRACK2,jweEncryptDecrypt(getReqTrack2()+"",false,false) ? jweDataEncryptDecrypt.getDataEncrypt(): "");
                request.put(typeTrans == 1 ? REMITTERCARD :BENEFICIARYCARD ,jsonBeneficiaryCard);
            }catch (JSONException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                return null;
            }
            return request;
        }
}
