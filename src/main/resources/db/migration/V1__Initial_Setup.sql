CREATE SCHEMA route_service;
CREATE TABLE route_service.service_instances
(
  service_id FLOAT PRIMARY KEY,
  service VARCHAR(100) NOT NULL,
  plan VARCHAR(100) ,
  organization_guid VARCHAR(100) NOT NULL,
  space_guid VARCHAR(100)  NOT NULL
);

CREATE TABLE route_service.route_info
(
route_id FLOAT PRIMARY KEY ,
route_name VARCHAR(500) NOT NULL,
service_id FLOAT REFERENCES route_service.service_instances(service_id) ON UPDATE CASCADE  ON DELETE CASCADE,
bind_id FLOAT NOT NULL
);

CREATE TABLE route_service.filter_info
(
filter_id INTEGER PRIMARY KEY ,
filter_name VARCHAR(50) NOT NULL
);

CREATE TABLE route_service.filters_to_route
(
route_id FLOAT REFERENCES route_service.route_Info(route_id) ON UPDATE CASCADE  ON DELETE CASCADE,
app_guid VARCHAR(500) NOT NULL,
filter_id INTEGER REFERENCES route_service.filter_Info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE,
PRIMARY KEY (route_id,filter_id)
);

CREATE TABLE route_service.problem_description
(
  problem_id FLOAT PRIMARY KEY,
  filter_id INTEGER REFERENCES route_service.filter_Info(filter_id) ON UPDATE CASCADE  ON DELETE CASCADE,
  description VARCHAR(500) NOT NULL
);

CREATE TABLE route_service.additional_info
(
  id FLOAT PRIMARY KEY,
  source_url VARCHAR(100),
  destination_url VARCHAR(100),
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
(1, 'Default'),
(2, 'Directory_Traversal'),
(3, 'Authentication_Bypass'),
(4,'Sql_Injection'),
(5,'XSS');