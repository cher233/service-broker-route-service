CREATE SCHEMA route_service;
CREATE TABLE route_service.service_instances
(
  id SERIAL PRIMARY KEY,
  service_id VARCHAR(36) NOT NULL,
  plan_id VARCHAR(36) NOT NULL,
  organization_guid VARCHAR(36) NOT NULL,
  space_guid VARCHAR(36) NOT NULL
);

CREATE TABLE route_service.route_info
(
route_id SERIAL PRIMARY KEY ,
route_name VARCHAR(225) NOT NULL,
service_id INTEGER NOT NULL REFERENCES route_service.service_instances(id) ON UPDATE CASCADE  ON DELETE CASCADE,
binding_id VARCHAR(36) NOT NULL
);

CREATE TABLE route_service.filter_info
(
filter_id INTEGER PRIMARY KEY ,
filter_name VARCHAR(100) NOT NULL
);

CREATE TABLE route_service.filters_to_route
(
id SERIAL PRIMARY KEY,
route_id INTEGER NOT NULL REFERENCES route_service.route_info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
app_guid VARCHAR(36),
filter_id INTEGER NOT NULL  REFERENCES route_service.filter_info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE
);

CREATE TABLE route_service.problem_description
(
  problem_id SERIAL PRIMARY KEY,
  filter_id INTEGER NOT NULL REFERENCES route_service.filter_info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  description Text NOT NULL
);

CREATE TABLE route_service.additional_info
(
  id SERIAL PRIMARY KEY,
  source_url VARCHAR(225),
  destination_url VARCHAR(225),
  time_of_problem TIME NOT NULL
);

CREATE TABLE route_service.filter_findings
(
  id SERIAL PRIMARY KEY,
  route_id INTEGER NOT  NULL REFERENCES route_service.route_Info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  problem_id INTEGER NOT NULL REFERENCES route_service.problem_description(problem_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  additional_info INTEGER REFERENCES route_service.additional_info(id)ON UPDATE CASCADE  ON DELETE CASCADE,
  fixed boolean NOT NULL
);

INSERT INTO route_service.filter_Info(filter_id, filter_name) VALUES
(0, 'default'),
(1, 'directory_traversal'),
(2, 'authentication_bypass'),
(3,'sql_injection'),
(4,'xss');