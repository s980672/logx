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


    public final static String getQueryServiceAPPPV(String start, String end){
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
                "      \"reqDt\": {\n" +
                "        \"gte\": \"" + start + "\",\n" +
                "        \"lt\": \"" + end + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  \n" +
                "}";
    }


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

}
