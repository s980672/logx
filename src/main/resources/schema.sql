CREATE TABLE if not exists `logx`.`service` (
  `name` varchar(255) NOT NULL,
  `svc_id` varchar(255) NOT NULL,
  PRIMARY KEY (`svc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;