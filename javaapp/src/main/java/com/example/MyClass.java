package com.example;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class MyClass {

    private static String json = "{\"kw_class_name\":\"Home\",\"kw_method_name_list\":[{\"name\":\"Banners\",\"return_type\":\"BannerBean\",\"params_type\":\"Params\",\"url\":\"http://www.aglhz.com:8090/sub_property_ysq/neighbor/activity/to-client/activity-list\",\"url_params\":[{\"name\":\"pageSize\"},{\"name\":\"page\"},{\"name\":\"keywords\"},{\"name\":\"cmnt_c\"}]},{\"name\":\"Notice\",\"return_type\":\"BaseBean\",\"params_type\":\"Params\",\"url\":\"http://www.aglhz.com:8090/sub_property_ysq/neighbor/activity/to-client/activity-list\",\"url_params\":[{\"name\":\"pageSize\"},{\"name\":\"page\"},{\"name\":\"keywords\"},{\"name\":\"cmnt_c\"}]}],\"fragment_layout\":\"fragment_home\"}";
    static ConfigBean bean = formatJson();

    static String ROOT_PATH = "F:\\gaocheng\\code\\auto\\";

    public static void main(String[] args) {

        String c_moethView = "";
        String c_moethPresenter = "";
        String c_moethModel = "";

        String m_mothod = "";

        String p_mothod = "";

        String f_mothod = "";


        String api_method = "";

        for (int i = 0; i < bean.getKw_method_name_list().size(); i++) {
            ConfigBean.KwMethodNameListBean methodBean = bean.getKw_method_name_list().get(i);
            String methodName = methodBean.getName();
            String methodType = methodBean.getParams_type() + " " + methodBean.getParams_type().toLowerCase();
            String methodReturn = methodBean.getReturn_type();
            //------------------ contract -------------------
            c_moethView += String.format(TempCode.CONTRACT_METHOD_VIEW, methodName, methodReturn + " bean");
            c_moethPresenter += String.format(TempCode.CONTRACT_METHOD_PRESENTER, methodName, methodType);
            c_moethModel += String.format(TempCode.CONTRACT_METHOD_MODEL, methodName, methodType, methodReturn);

            //------------------ model -------------------
            String paramsStr = "";
            String api_params = "";
            for (int j = 0; j < methodBean.getUrl_params().size(); j++) {
                String name = methodBean.getUrl_params().get(j).getName();
                paramsStr += methodBean.getParams_type().toLowerCase() + "." + name;
                api_params += String.format(TempCode.API_PARAMS, name);
                if (j != methodBean.getUrl_params().size() - 1) {
                    paramsStr += ",";
                }
            }
            m_mothod += String.format(TempCode.MODEL_METHOD, methodReturn, methodName, methodType, paramsStr);

            //------------------ api -------------------
            api_method += String.format(TempCode.API,methodName,methodBean.getUrl(),methodReturn,api_params);

            //------------------ presenter -------------------
            p_mothod += String.format(TempCode.PRESENTER_METHOD, methodName);

            //------------------ fragment -------------------
            f_mothod += String.format(TempCode.FRAGMENT_RESPONSE_METHOD, methodName, methodReturn + " bean");

        }
        System.out.print("-------------------- contract -----------------------\n");
        String contractStr = String.format(TempCode.CONTRACT,
                bean.getKw_class_name(), c_moethView, c_moethPresenter, c_moethModel);
        System.out.print(contractStr);
        writeToFile(ROOT_PATH + bean.getKw_class_name() + "Contract.java", contractStr);

        System.out.print("-------------------- api -----------------------\n");
        System.out.print(api_method);
        writeToFile(ROOT_PATH + "ApiService.java", api_method);

        System.out.print("---------------------- model ------------------------\n");
        contractStr = String.format(TempCode.MODEL, bean.getKw_class_name(), m_mothod);
        System.out.print(contractStr);
        writeToFile(ROOT_PATH + bean.getKw_class_name() + "Model.java", contractStr);

        System.out.print("-------------------- presenter ----------------------\n");
        contractStr = String.format(TempCode.PRESENTER, bean.getKw_class_name(), p_mothod);
        System.out.print(contractStr);
        writeToFile(ROOT_PATH + bean.getKw_class_name() + "Presenter.java", contractStr);

        System.out.print("-------------------- fragment ----------------------\n");
        contractStr = String.format(TempCode.FRAGMENT, bean.getKw_class_name(), bean.getFragment_layout(), f_mothod);
        System.out.print(contractStr);
        writeToFile(ROOT_PATH + bean.getKw_class_name() + "Fragment.java", contractStr);
    }

    private static ConfigBean formatJson() {
        Gson gson = new Gson();
        return gson.fromJson(json, ConfigBean.class);
    }

    private static void writeToFile(String fileName, String content) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
