CREATE TABLE app_user
-- 1 step
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    password_hash TEXT                NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    role          VARCHAR(10) DEFAULT 'USER',
    created_at    TIMESTAMP   DEFAULT NOW(),
    image_url TEXT DEFAULT 'uploads/default_icon.jpg'
);

CREATE TABLE recipe
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    calories    INT,
    user_id     INT REFERENCES app_user (id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT NOW(),
    image_url TEXT DEFAULT 'uploads/healthy-food.jpg'
);


-- 2 step
INSERT INTO app_user (username, password_hash, email, role)
VALUES
    ('admin', '$2a$12$Ev37VnPTPXVgNoRQxMfgpOKEb6rrPZDZiQyCP/iqgPVwhqlrOE5s6', 'john.doe@mail.com', 'admin'),
    ('alice.jones', '$2a$12$ePebG4ZMPBawW7bqk6WhYeHVXTEfVfyhQW6U2pjiKji/HYg8yALF.', 'alice.jones@mail.com', 'user'),
    ('bob.smith', '$2a$12$Kh9a520nW4zvfUOBv673p.FwjeHDYNJmGgTYA6s6lhSQVDJMHRn/u', 'bob.smith@mail.com', 'user'),
    ('charlie.brown', '$2a$12$55kdkmdYd8RHWVS2TqDJKuOMFcdjBckGp4G3Hhr47Xo3BcpvivWhm', 'charlie.brown@mail.com', 'user'),
    ('david.martin', '$2a$12$55kdkmdYd8RHWVS2TqDJKuOMFcdjBckGp4G3Hhr47Xo3BcpvivWhm', 'david.martin@mail.com', 'user'),
    ('emma.white', '$2a$12$6CrIfAtyzuiPWOgKPYaftOfcKjsr0OTBaYCx5Pq3/fpnF5PnfnMVO', 'emma.white@mail.com', 'user'),
    ('frank.green', '$2a$12$QSeyzTi3hyU9OrT0AhiH7eOb8dgZKCzkL1xR0vO2sj7DQFWjEh84q', 'frank.green@mail.com', 'user'),
    ('grace.lee', '$2a$12$sklEm4.KOwWRBwvljOzsEO86mQ/F07jgVhwZpWMzrKwFvXiEXLvtW', 'grace.lee@mail.com', 'user'),
    ('henry.kim', '$2a$12$FE0Wtx4VRaC7YrOxxnJbWuaY3pa0oVRhLE2lk/fzT1E3wS8qU0LKa', 'henry.kim@mail.com', 'user');

--  3 step
INSERT INTO recipe ( title, description, calories, user_id)
VALUES
    ('Chicken Salad', 'A healthy chicken salad made with grilled chicken, mixed greens, cucumbers, cherry tomatoes, and a light vinaigrette dressing. Perfect for a refreshing lunch or a light dinner.', 350, 3),
    ('Avocado Toast', 'Creamy mashed avocado spread on toasted whole-grain bread, topped with a sprinkle of salt, pepper, and a drizzle of olive oil. A quick, healthy breakfast or snack option.', 400, 5),
    ('Spaghetti Carbonara', 'A classic Italian pasta dish made with spaghetti, eggs, pancetta, garlic, and Parmesan cheese. The rich sauce is created by combining eggs and cheese, giving it a creamy texture without cream.', 600, 4),
    ('Grilled Cheese Sandwich', 'A nostalgic grilled cheese sandwich with golden, crispy bread and gooey, melted cheddar cheese. It’s simple comfort food, perfect when paired with tomato soup.', 500, 6),
    ('Caesar Salad', 'Crisp romaine lettuce tossed in a creamy Caesar dressing, garnished with crunchy croutons and shaved Parmesan cheese. It’s a classic side dish or main for a light meal.', 300, 7),
    ('Vegetable Stir-Fry', 'A vibrant and healthy stir-fry made with a mix of fresh vegetables like bell peppers, broccoli, and carrots, all cooked in a savory soy sauce with garlic and ginger. Serve over rice or noodles for a quick, nutritious meal.', 250, 8),
    ('Beef Tacos', 'Seasoned beef filling soft tortillas with crisp lettuce, juicy tomatoes, cheddar cheese, and a dollop of salsa. These tacos are perfect for a fun dinner with friends or family.', 550, 9),
    ('Chicken Burrito', 'A hearty burrito packed with seasoned grilled chicken, rice, black beans, and cheese, all wrapped up in a soft flour tortilla. It’s a complete meal that’s easy to eat on the go.', 700, 2),
    ('Margarita Pizza', 'A traditional Neapolitan pizza topped with fresh mozzarella, vine-ripened tomatoes, and fresh basil leaves. The thin crust is lightly baked, and the simplicity of the toppings makes this pizza a timeless favorite.', 800, 6),
    ( 'Grilled Salmon', 'Grilled salmon fillets seasoned with lemon, dill, and olive oil, then cooked until flaky and tender. Served with roasted potatoes and a side of steamed vegetables, it’s a light, protein-packed meal.', 450, 2),
    ( 'Pasta Primavera', 'Pasta tossed with a medley of sautéed vegetables like zucchini, bell peppers, and tomatoes, finished with a light garlic and olive oil sauce. A fresh, flavorful vegetarian dish.', 550, 3),
    ( 'Eggplant Parmesan', 'Breaded eggplant slices, fried to golden perfection, then layered with marinara sauce and melted mozzarella cheese. Baked until bubbly and served with spaghetti or a side salad.', 600, 4),
    ( 'Chicken Tikka Masala', 'A flavorful Indian curry made with tender chicken pieces in a rich, creamy tomato sauce spiced with garam masala, cumin, and coriander. Serve with basmati rice or naan bread for a complete meal.', 700, 5),
    ( 'Quinoa Salad', 'A light and refreshing salad made with cooked quinoa, cherry tomatoes, cucumbers, and red onion, dressed with a tangy lemon vinaigrette. It’s a perfect side dish or a healthy meal on its own.', 350, 6),
    ( 'Chocolate Cake', 'A decadent, rich chocolate cake made with cocoa powder, eggs, and butter, topped with a smooth chocolate ganache. The ultimate dessert for any occasion, guaranteed to satisfy your sweet tooth.', 550, 7),
    ( 'Pancakes', 'Fluffy, soft pancakes served with melted butter and a generous drizzle of maple syrup. These are perfect for a weekend breakfast or brunch with your favorite toppings like berries or whipped cream.', 400, 8),
    ( 'Chicken Noodle Soup', 'A comforting soup made with tender chicken, carrots, celery, and noodles, all simmered in a savory broth. It’s the perfect dish to warm you up on a chilly day or when feeling under the weather.', 300, 9),
    ( 'BBQ Ribs', 'Slow-cooked pork ribs glazed with a smoky, tangy BBQ sauce and grilled until perfectly caramelized. Served with coleslaw and cornbread, it’s a hearty meal that’s perfect for a summer cookout.', 800, 2),
    ( 'Smoothie Bowl', 'A refreshing smoothie bowl made with blended berries, banana, and almond milk, topped with granola, chia seeds, and fresh fruit. A nutritious breakfast or snack that’s as beautiful as it is delicious.', 250, 2),
    ( 'Lentil Soup', 'A hearty and filling soup made with green lentils, tomatoes, carrots, onions, and garlic. It’s seasoned with a blend of spices like cumin and turmeric for a flavorful, nutritious meal.', 350, 3);

--
