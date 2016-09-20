package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.AppViewRepository;
import com.sktechx.palab.logx.secondary.domain.Asset;
import com.sktechx.palab.logx.secondary.domain.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 1002382 on 2016. 8. 10..
 */
@Service
public class ChangeNameIdService {

    Logger logger = LoggerFactory.getLogger(ChangeNameIdService.class);

    @Autowired
    AssetRepository assetRepo;

    @Autowired
    AppViewRepository appRepo;

    public String getSvcName(String svcId) {
        if ( svcId.equals("ALL") ){
            return "전체서비스";
        }
        Asset one = assetRepo.findOne(Long.parseLong(svcId));
        if (one != null)
            return one.getName();

        return "전체서비스";

    }

    public void fillNameOrIdOfAppOrSvc(enumOptionType opType, Object data) {


        switch (opType) {
            case APP:
                List<SvcOption1RC> lstApp = (List<SvcOption1RC>) data;
                fillAppIdAndName(opType, lstApp);
                fillServiceNameForOption1(lstApp);
                break;
            case API:
            case ERROR:
                List<SvcOption1RC> lst = (List<SvcOption1RC>) data;
                fillServiceNameForOption1(lst);
                break;
            case API_APP:
                List<SvcOption2RC> lst2 = (List<SvcOption2RC>) data;
                fillAppIdAndName(opType, lst2);
                fillServiceNameForOption2(lst2);
                break;

            case SVC:
                List<ServiceRequestCall> lstSvc = (List<ServiceRequestCall>) data;
                fillServiceName(lstSvc);
                break;
            //아래는 service는 필요 없음
            case ERROR_APP:
            case APP_API:
                List<SvcOption2RC> lstApp2 = (List<SvcOption2RC>) data;
                fillAppIdAndName(opType, lstApp2);
                break;
        }

    }

    private void fillAppIdAndName(enumOptionType opType, Object lst) {

        List<AppView> apps = appRepo.findAll();
        if (opType == enumOptionType.APP_API) {

            List<SvcOption2RC> lstApp = (List<SvcOption2RC>) lst;
            lstApp.stream().map(app -> app.getId().getOption1()).distinct().forEach(appKey -> {
                apps.stream().filter(app -> app.getAppKey().equals(appKey)).forEach(app -> {

                    lstApp.stream().filter(rc -> rc.getId().getOption1().equals(app.getAppKey())).forEach(rc -> {
                        rc.setAppId(app.getAppId());
                        rc.setAppName(app.getName());
                    });
                });
            });
        }
        else if (opType == enumOptionType.ERROR_APP || opType==enumOptionType.API_APP ) {
            List<SvcOption2RC> lstApp = (List<SvcOption2RC>) lst;
            lstApp.stream().map(app -> app.getId().getOption2()).distinct().forEach(appKey -> {
                apps.stream().filter(app -> app.getAppKey().equals(appKey)).forEach(app -> {

                    lstApp.stream().filter(rc -> rc.getId().getOption2().equals(app.getAppKey())).forEach(rc -> {
                        rc.setAppId(app.getAppId());
                        rc.setAppName(app.getName());
                    });
                });
            });
        } else if (opType == enumOptionType.APP) {
            List<SvcOption1RC> lstApp = (List<SvcOption1RC>) lst;
            lstApp.stream().map(app -> app.getId().getOption1()).distinct().forEach(appKey -> {
                apps.stream().filter(app -> app.getAppKey().equals(appKey)).forEach(app -> {

                    lstApp.stream().filter(rc -> rc.getId().getOption1().equals(app.getAppKey())).forEach(rc -> {
                        rc.setAppId(app.getAppId());
                        rc.setAppName(app.getName());
                    });
                });
            });
        }

    }


    private void fillServiceName(List<ServiceRequestCall> lst) {

        List<Asset> svcs = assetRepo.findAll();

        lst.stream().map(rc -> rc.getId().getSvcId()).distinct().forEach(svcId -> {
            svcs.stream().filter(svc -> svcId.equals(svc.getId()+"")).forEach(s -> {

                        lst.stream().filter(rc -> rc.getId().getSvcId().equals(s.getId()+"")).forEach(rc -> {
                                    rc.setSvcName(s.getName());
                                }
                        );
                    }
            );
        });
    }

    private void fillServiceNameForOption1(List<SvcOption1RC> lst) {

        List<Asset> svcs = assetRepo.findAll();

        lst.stream().map(rc -> rc.getId().getSvcId()).distinct().forEach(svcId -> {
            svcs.stream().filter(svc -> svcId.equals(svc.getId()+"")).forEach(s -> {

                        lst.stream().filter(rc -> rc.getId().getSvcId().equals(s.getId()+"")).forEach(rc -> {
                                    rc.setSvcName(s.getName());
                                }
                        );
                    }
            );
        });
    }

    private void fillServiceNameForOption2(List<SvcOption2RC> lst) {

        List<Asset> svcs = assetRepo.findAll();

        lst.stream().map(rc -> rc.getId().getSvcId()).distinct().forEach(svcId -> {
            svcs.stream().filter(svc -> svcId.equals(svc.getId()+"")).forEach(s -> {

                        lst.stream().filter(rc -> rc.getId().getSvcId().equals(s.getId()+"")).forEach(rc -> {
                                    rc.setSvcName(s.getName());
                                }
                        );
                    }
            );
        });
    }
}