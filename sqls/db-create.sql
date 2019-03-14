create database notifier;
use notifier;

CREATE TABLE notifier_emails (
  id mediumint(9) NOT NULL auto_increment,
  email	varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE notifier_email_data (
  id mediumint(9) NOT NULL auto_increment,
  title	varchar(255) NOT NULL,
  subject VARCHAR(255) NOT NULL,
  date VARCHAR(255) NOT NULL,
  body TEXT,
  email_to TEXT,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;