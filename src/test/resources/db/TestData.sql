INSERT INTO public.year (id, year) VALUES (1, 2018);
INSERT INTO public.year (id, year) VALUES (2, 2019);
INSERT INTO public.year (id, year) VALUES (3, 2004);
INSERT INTO public.year (id, year) VALUES (4, 2005);

INSERT INTO public.category (id, name) VALUES (1, 'Sedan');
INSERT INTO public.category (id, name) VALUES (2, 'Pickup');
INSERT INTO public.category (id, name) VALUES (3, 'SUV');
INSERT INTO public.category (id, name) VALUES (4, 'Wagon');

INSERT INTO public.manufacturer (id, name) VALUES (1, 'Toyota');
INSERT INTO public.manufacturer (id, name) VALUES (2, 'Volvo');
INSERT INTO public.manufacturer (id, name) VALUES (3, 'BMW');

INSERT INTO public.model (id, name, manufacturer_id) VALUES (1, 'XC70', 2);
INSERT INTO public.model (id, name, manufacturer_id) VALUES (2, '6 Series', 3);
INSERT INTO public.model (id, name, manufacturer_id) VALUES (3, '7 Series', 3);
INSERT INTO public.model (id, name, manufacturer_id) VALUES (4, 'M3', 3);
INSERT INTO public.model (id, name, manufacturer_id) VALUES (5, 'XC40', 2);
INSERT INTO public.model (id, name, manufacturer_id) VALUES (6, 'Tundra Double Cab', 1);

INSERT INTO public.model_category (model_id, category_id) VALUES (1, 3);
INSERT INTO public.model_category (model_id, category_id) VALUES (5, 3);
INSERT INTO public.model_category (model_id, category_id) VALUES (3, 1);
INSERT INTO public.model_category (model_id, category_id) VALUES (2, 4);
INSERT INTO public.model_category (model_id, category_id) VALUES (1, 4);
INSERT INTO public.model_category (model_id, category_id) VALUES (4, 1);
INSERT INTO public.model_category (model_id, category_id) VALUES (6, 2);

INSERT INTO public.model_year (model_id, year_id) VALUES (5, 2);
INSERT INTO public.model_year (model_id, year_id) VALUES (1, 2);
INSERT INTO public.model_year (model_id, year_id) VALUES (3, 1);
INSERT INTO public.model_year (model_id, year_id) VALUES (6, 4);
INSERT INTO public.model_year (model_id, year_id) VALUES (6, 2);
INSERT INTO public.model_year (model_id, year_id) VALUES (2, 3);
INSERT INTO public.model_year (model_id, year_id) VALUES (4, 1);
INSERT INTO public.model_year (model_id, year_id) VALUES (1, 4);

INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (1, '8ZtzCENmXb', 1, 2, 6, 2);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (2, 'hqczihz66a', 2, 2, 1, 3);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (3, 'Kb9b2493oy', 2, 2, 5, 3);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (4, 'SLYFpQdCVH', 3, 1, 3, 1);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (5, 'WHCTL8PKc2', 3, 1, 4, 1);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (6, '8ZtPCENmXb', 1, 4, 6, 2);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (7, 'jwdXFQAFx6', 2, 4, 1, 4);
INSERT INTO public.car (id, object_id, manufacturer_id, year_id, model_id, category_id) VALUES (8, 'lzF2TNp55g', 3, 3, 2, 4);
