CREATE TABLE `warehouses` (
  `id` integer PRIMARY KEY,
  `warehouseName` varchar(255) UNIQUE NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `comapanies` (
  `id` integer PRIMARY KEY,
  `warehouse_id` integer,
  `companyName` varchar(255) UNIQUE NOT NULL,
  `street` varchar(255),
  `city` varchar(255),
  `pincode` varchar(255),
  `country` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `products` (
  `id` integer PRIMARY KEY,
  `company_id` integer,
  `productName` varchar(255) UNIQUE NOT NULL,
  `description` varchar(255),
  `mfd_date` datetime,
  `exp_date` datetime,
  `quantity` integer NOT NULL,
  `price` double NOT NULL,
  `productImage` blob NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` integer PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `phone` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `street` varchar(255),
  `pincode` varchar(255)
);

CREATE TABLE `shipments` (
  `id` integer PRIMARY KEY,
  `product_id` integer NOT NULL,
  `order_id` integer NOT NULL,
  `user_id` integer NOT NULL,
  `quanty_ordered` integer NOT NULL,
  `date_of_order` datetime NOT NULL,
  `price_per_unit` double NOT NULL,
  `total_price` double NOT NULL
);

CREATE TABLE `orders` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `order_date` datetime NOT NULL,
  `user_id` integer NOT NULL,
  `total_order_price` double NOT NULL
);

ALTER TABLE `comapanies` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);

ALTER TABLE `products` ADD FOREIGN KEY (`company_id`) REFERENCES `comapanies` (`id`);

ALTER TABLE `shipments` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

ALTER TABLE `shipments` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `shipments` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

ALTER TABLE `orders` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
