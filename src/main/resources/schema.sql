CREATE OR REPLACE VIEW `logx`.`view_app` AS
  select `b`.`app_key` AS `app_key`,`a`.`id` AS `app_id`,`a`.`name` AS `name` from (`apix`.`application_aud` `a` join `apix`.`appkey_aud` `b`)
  where (`a`.`appkey_id` = `b`.`id`) and (`a`.`revtype` = 0) and (`b`.`revtype` <> 2 ) order by `a`.`id` ;


