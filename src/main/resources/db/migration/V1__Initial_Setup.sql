CREATE SCHEMA route_service;

CREATE TABLE route_service.route_Info
(
route_id INTEGER PRIMARY KEY ,
route VARCHAR(500) NOT NULL
);

CREATE TABLE route_service.filter_Info
(
filter_id INTEGER PRIMARY KEY ,
filter_name VARCHAR(500) NOT NULL
);

CREATE TABLE route_service.filter_to_route
(
route_id INTEGER REFERENCES route_Info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
app_guid VARCHAR(500) NOT NULL,
filter_id INTEGER REFERENCES filter_Info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE,
PRIMARY KEY (route_id, filter_id)
);

CREATE TABLE route_service.service_instances
(
service_id INTEGER PRIMARY KEY,
plan VARCHAR(500) ,
organization_guid VARCHAR(100) NOT NULL,
space_guid VARCHAR(100)  NOT NULL
);

CREATE TABLE route_service.problem_description
(
problem_id INTEGER PRIMARY KEY,
filter_id INTEGER REFERENCES filter_Info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE,
description VARCHAR(500) NOT NULL
);

CREATE TABLE route_service.filter_findings
(
id INTEGER PRIMARY KEY,
route_id INTEGER NOT  NULL REFERENCES route_Info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
problem_id INTEGER NOT NULL REFERENCES problem_description(problem_id) ON UPDATE CASCADE  ON DELETE CASCADE,
fixed boolean NOT NULL
);

CREATE TABLE route_service.additional_info
(
  id INTEGER PRIMARY KEY,
  source_url VARCHAR(100),
  destination_url VARCHAR(100),
  time_of_problem TIME NOT NULL
);

INSERT INTO filter_Info(filter_id, filter_name) VALUES
(1, 'Directory_Traversal'),(2, 'Authentication_Bypass'),(3,'Sql_Injection'),(4,'XSS');