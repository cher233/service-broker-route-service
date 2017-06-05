CREATE SCHEMA route_service;
CREATE TABLE route_service.service_instances
(
  id FLOAT PRIMARY KEY,
  service_id VARCHAR(225) NOT NULL,
  plan VARCHAR(225) ,
  organization_guid VARCHAR(225) NOT NULL,
  space_guid VARCHAR(225) NOT NULL
);

CREATE TABLE route_service.route_info
(
route_id FLOAT PRIMARY KEY ,
route_name VARCHAR(225) NOT NULL,
service_id FLOAT NOT NULL REFERENCES route_service.service_instances(id) ON UPDATE CASCADE  ON DELETE CASCADE,
binding_id VARCHAR(225) NOT NULL
);

CREATE TABLE route_service.filter_info
(
filter_id INTEGER PRIMARY KEY ,
filter_name VARCHAR(225) NOT NULL
);

CREATE TABLE route_service.filters_to_route
(
id FLOAT PRIMARY KEY,
route_id FLOAT NOT NULL REFERENCES route_service.route_info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
app_guid VARCHAR(225),
filter_id INTEGER NOT NULL
);

CREATE TABLE route_service.problem_description
(
  problem_id FLOAT PRIMARY KEY,
  filter_id INTEGER NOT NULL ,
  description VARCHAR(225) NOT NULL
);

CREATE TABLE route_service.additional_info
(
  id FLOAT PRIMARY KEY,
  source_url VARCHAR(225),
  destination_url VARCHAR(225),
  time_of_problem TIME NOT NULL
);

CREATE TABLE route_service.filter_findings
(
  id FLOAT PRIMARY KEY,
  route_id INTEGER NOT  NULL REFERENCES route_service.route_Info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  problem_id FLOAT NOT NULL REFERENCES route_service.problem_description(problem_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  additional_info FLOAT REFERENCES route_service.additional_info(id)ON UPDATE CASCADE  ON DELETE CASCADE,
  fixed boolean NOT NULL
);

INSERT INTO route_service.filter_Info(filter_id, filter_name) VALUES
(1, 'default'),
(2, 'directory_traversal'),
(3, 'authentication_bypass'),
(4,'sql_injection'),
(5,'xss');