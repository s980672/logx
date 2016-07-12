package com.sktechx.palab.logx.service;

/**
 * Created by 1002382 on 2016. 7. 7..
 */
public class AggReqDSLs {

    public final static String getQueryPV(String interval) {

        return "{\n" +
                "  \"aggs\": {\n" +
                "    \"serviceRC_over_time\": {\n" +
                "      \"date_histogram\": {\n" +
                "        \"field\": \"@timestamp\",\n" +
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
    public final static String getQueryPVDuringPeriod(long start, long end) {

        return "{\n" +
                "  \"query\": {\n" +
                "    \"range\": {\n" +
                "      \"@timestamp\": {\n" +
                "        \"lt\": " + end + " ,\n" +
                "        \"gte\": " + start + "\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

    }

    /*
    start/end는 epoch이후에 millisecond 단위
    일배치에 사용
     */
    public final static String getQueryServicePV(long start, long end) {

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
                "      \"@timestamp\": {\n" +
                "        \"lt\": " + end + " ,\n" +
                "        \"gte\": " + start + "\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

    }


    public final static String getQueryServiceAPPPV(long start, long end){
        return "{\n" +
                "  \"aggs\" : {\n" +
                "    \"serviceRC\" : {\n" +
                "      \"terms\" : {\n" +
                "        \"field\" : \"svcId\"\n" +
                "      },\n" +
                "      \"aggs\" : {\n" +
                "        \"appRC\" : {\n" +
                "          \"terms\" : {\n" +
                "            \"field\" : \"appId\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "     }\n" +
                "  },\n" +
                "  \"query\" : {\n" +
                "    \"range\" : {\n" +
                "      \"@timestamp\": {\n" +
                "        \"gte\": " + start + ",\n" +
                "        \"lt\": " + end + "\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  \n" +
                "}";
    }


    public final static String getQueryServiceUV(long start, long end) {
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
                "      \"@timestamp\": {\n" +
                "        \"gte\": " + start + ",\n" +
                "        \"lt\": " + end + "\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

}
