-- src/main/resources/data.sql

-- Insere regiões apenas se elas não existirem (evita duplicação)
INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LPL', 'lpl', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lpl') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LEC', 'lec', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lec') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LCK', 'lck', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lck') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LCP', 'lcp', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lcp') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'PCS', 'pcs', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'pcs') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'VCS', 'vcs', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'vcs') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'CBLOL', 'cblol', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'cblol') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LLA', 'lla', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lla') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LJL', 'ljl', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'ljl') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'TCL', 'tcl', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'tcl') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LCO', 'lco', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lco') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LCL', 'lcl', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lcl') LIMIT 1;

INSERT INTO regions (name, slug, is_active)
SELECT * FROM (SELECT 'LCS', 'lcs', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM region WHERE slug = 'lcs') LIMIT 1;