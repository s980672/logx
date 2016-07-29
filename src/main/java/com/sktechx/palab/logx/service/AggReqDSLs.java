package com.sktechx.palab.logx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 1002382 on 2016. 7. 7..
 */
public class AggReqDSLs {

    private static Logger logger = LoggerFactory.getLogger(AggReqDSLs.class);

    public final static String getQueryPV(String interval) {

        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"serviceRC_over_time\": {\n" +
                "      \"date_histogram\": {\n" +
                "        \"field\": \"reqDt\",\n" +
                "        \"interval\": \"" + interval + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    /*
    start/end는 epoch이후에 millisecond 단위
    일배치에 사용
     */
    public final static String getQueryPVDuringPeriod(String start, String end) {



        String str ="{\n" +
                "  \"query\": {\n" +
                "    \"range\": {\n" +
                "      \"reqDt\": {\n" +
                "        \"gte\": \"" + start + "\",\n" +
                "        \"lt\": \"" + end + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        logger.debug(str);

        return str;

    }

    /*
    start/end는 epoch이후에 millisecond 단위
    일배치에 사용
     */
    public final static String getQueryServicePV(String start, String end) {

        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"serviceRC\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"svcId\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"range\": {\n" +
                "      \"reqDt\": {\n" +
                "        \"gte\": \"" + start + "\", \n" +
                "        \"lt\": \"" + end + "\" \n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

    }


    public final static String getQueryServiceOption1PV(String otpion1Field, String start, String end){
        return "{\n" +
                "  \"aggs\" : {\n" +
                "    \"serviceRC\" : {\n" +
                "      \"terms\" : {\n" +
                "        \"field\" : \"svcId\"\n" +
                "      },\n" +
                "      \"aggs\" : {\n" +
                "        \"option1RC\" : {\n" +
                "          \"terms\" : {\n" +
                "            \"field\" : \""+otpion1Field+"\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "     }\n" +
                "  },\n" +
                "  \"query\" : {\n" +
                "    \"range\" : {\n" +
                "      \"reqDt\": {\n" +
                "        \"gte\": \"" + start + "\",\n" +
                "        \"lt\": \"" + end + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  \n" +
                "}";
    }
//
//
//    public final static String getQueryServiceOption2PV(String otpion1Field,String otpion2Field, String start, String end){
//        return "{\n" +
//                "  \"aggs\" : {\n" +
//                "    \"serviceRC\" : {\n" +
//                "      \"terms\" : {\n" +
//                "        \"field\" : \"svcId\"\n" +
//                "      },\n" +
//                "      \"aggs\" : {\n" +
//                "        \"option1RC\" : {\n" +
//                "          \"terms\" : {\n" +
//                "            \"field\" : \""+otpion1Field+"\"\n" +
//                "          },\n" +
//                "      		\"aggs\" : {\n" +
//                "       	 	\"option2RC\" : {\n" +
//                "        	  	\"terms\" : {\n" +
//                "          		\"field\" : \""+otpion2Field+"\"\n" +
//                "          			}\n" +
//                "        		}\n" +
//                "     		 }\n" +
//                "        }\n" +
//                "      }\n" +
//                "     }\n" +
//                "  },\n" +
//                "  \"query\" : {\n" +
//                "    \"range\" : {\n" +
//                "      \"reqDt\": {\n" +
//                "        \"gte\": \"" + start + "\",\n" +
//                "        \"lt\": \"" + end + "\"\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "  \n" +
//                "}";
//    }

    public final static String getQueryServiceUV(String start, String end) {
        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"svcRC\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"svcId\"\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"uvCount\": {\n" +
                "          \"terms\": {\n" +
                "            \"field\": \"cltIp\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"range\": {\n" +
                "      \"reqDt\": {\n" +
                "        \"gte\": \"" + start + "\",\n" +
                "        \"lt\": \"" + end + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    /*
    reponse code 200대의 성공 요청은 제외한다
     */
    public static String getQueryErrorCount(String start, String end) {
        return
                "{\n" +
                "  \"aggs\": {\n" +
                "    \"errorCount\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"responseCode\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
//                "      \"must_not\": [\n" +
//                "        {\n" +
//                "          \"terms\": {\n" +
//                "            \"responseCode\": [\"200\",\"201\"]\n" +
//                "          }\n" +
//                "        }\n" +
//                "      ],\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"reqDt\": {\n" +
                "              \"gte\": \"" + start + "\",\n" +
                "              \"lt\": \"" + end + "\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    public static String getQueryOption1Option2AllSvcPV(String option1, String option2, String start, String end){
        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"option1RC\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \""+option1+"\"\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"option2RC\": {\n" +
                "          \"terms\": {\n" +
                "            \"field\": \""+option2+"\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"reqDt\": {\n" +
                "              \"gte\": \"2016-07-17\",\n" +
                "              \"lt\": \"2016-07-18\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "} ";
    }

    public static String getQueryErrorSvcCount(String start, String end) {
        return
                "{\n" +
                "  \"aggs\" : {\n" +
                "    \"serviceRC\" : {\n" +
                "      \"terms\" : {\n" +
                "        \"field\" : \"svcId\"\n" +
                "      },\n" +
                "      \"aggs\" : {\n" +
                "        \"option1RC\" : {\n" +
                "          \"terms\" : {\n" +
                "            \"field\" : \"responseCode\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "     }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
//                "      \"must_not\": [\n" +
//                "        {\n" +
//                "          \"terms\": {\n" +
//                "            \"responseCode\": [\"200\",\"201\"]\n" +
//                "          }\n" +
//                "        }\n" +
//                "      ],\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"reqDt\": {\n" +
                "              \"gte\": \"" + start + "\",\n" +
                "              \"lt\": \"" + end + "\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "}";
    }

    public static String getQueryServiceOption2PV(String option1, String option2, String start, String end) {
        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"serviceRC\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"svcId\"\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"option1RC\": {\n" +
                "          \"terms\": {\n" +
                "            \"field\": \""+option1+"\"\n" +
                "          },\n" +
                "          \"aggs\": {\n" +
                "            \"option2RC\": {\n" +
                "              \"terms\": {\n" +
                "                \"field\": \""+option2+"\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"range\": {\n" +
                "            \"reqDt\": {\n" +
                "              \"gte\": \"" + start + "\",\n" +
                "              \"lt\": \"" + end + "\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
