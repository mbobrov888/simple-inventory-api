DROP TABLE IF EXISTS manufacturer cascade;

CREATE TABLE manufacturer (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  home_page VARCHAR(250) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL
);

INSERT INTO manufacturer VALUES
  (1, 'ACME Corporation', 'https://www.acme-corp.com', '(07) 5556 4321'),
  (2, 'QUT', 'https://www.qut.edu.au/', '(07) 3138 2000'),
  (3, 'BlueScope Steel', 'http://www.bluescopesteel.com.au/', '1800 800 789');

DROP TABLE IF EXISTS inventory_item cascade;

CREATE TABLE inventory_item (
  id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  release_date TIMESTAMP WITH TIME ZONE NOT NULL,
  manufacturer_id INT NOT NULL,
  FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(id)
);

INSERT INTO inventory_item (name, release_date, manufacturer_id) VALUES
  ('Fireworks by ACME', '2020-02-03 17:00:00+10:00', 1),
  ('Widget Adapter', '2020-05-01 00:00:00+10:00', 2),
  ('Stainless reinforced beam', '2020-06-05 11:00:00+10:00', 3);
