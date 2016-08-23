CREATE OR REPLACE VIEW `logx`.`view_svc` AS
  select tyk_svc_id as svc_id, name from `apixgwdb`.`asset` order by id
;

CREATE OR REPLACE VIEW `logx`.`view_app` AS
  select b.app_key, a.id as app_id, a.name
  from `apixgwdb`.`application` as a , `apix`.`appkey` as b where a.appkey_id = b.id
  order by a.id;
;


