-- check floor plans
SELECT apartment.name, floor_plan.bed, floor_plan.bath, floor_plan.price_from
FROM apartment LEFT JOIN address ON apartment.address_id = address.id
    LEFT JOIN floor_plan ON apartment.id = floor_plan.apt_id;

-- check images
SELECT apartment.name, image.link,image.description
FROM apartment LEFT JOIN address ON apartment.address_id = address.id
    LEFT JOIN image ON apartment.id = image.apt_id