CREATE OR REPLACE VIEW `logx`.`view_svc` AS
  select tyk_svc_id as svc_id, name from `apix`.`asset` order by id
;

CREATE OR REPLACE VIEW `logx`.`view_app` AS
  select b.app_key, a.id as app_id, a.name
  from `apix`.`application` as a , `apix`.`appkey` as b where a.appkey_id = b.id
  order by a.id;
;


CREATE TABLE if not exists `logx`.`view_svc` (
  `name` varchar(255) NOT NULL,
  `svc_id` varchar(255) NOT NULL,
  PRIMARY KEY (`svc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE if not exists `logx`.`view_app` (
  `app_key` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `app_id` varchar(255) NOT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
