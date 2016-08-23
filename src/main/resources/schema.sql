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
