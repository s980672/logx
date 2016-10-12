CREATE OR REPLACE VIEW `logx`.`view_app` AS
  select b.app_key, a.id as app_id, a.name
  from `apix`.`application` as a , `apix`.`appkey` as b where a.appkey_id = b.id
  order by a.id;
  ;

CREATE TABLE if not exists `logx`.`view_app` (
  `app_key` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `app_id` varchar(255) NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;