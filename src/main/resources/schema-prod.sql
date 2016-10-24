-- 삭제된 앱도 이름을 명시하기 위해서 aud 테이블에서 app name 정보를 가져옴
CREATE OR REPLACE VIEW `logx`.`view_app` AS
  select `b`.`app_key` AS `app_key`,`a`.`id` AS `app_id`,`a`.`name` AS `name` from (`apigwdb`.`application_aud` `a` join `apigwdb`.`appkey_aud` `b`)
  where (`a`.`appkey_id` = `b`.`id`) and (`a`.`revtype` = 0) and (`b`.`revtype` <> 2 ) order by `a`.`id` ;

