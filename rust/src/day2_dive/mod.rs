pub mod test;

pub struct Position {
    x: u32,
    z: u32,
    aim: u32,
}

impl Position {
    fn new() -> Self {
        Self { x: 0, z: 0, aim: 0 }
    }

    pub fn product(&self) -> u32 {
        self.x * self.z
    }
}

trait Command {
    fn run(&self, position: Position) -> Position {
        position // default command does nothing
    }
}

// ~functional interface, we can pass in closures where a Command would be expected
impl<T> Command for T
where
    T: Fn(Position) -> Position,
{
    fn run(&self, position: Position) -> Position {
        self(position)
    }
}

struct CommandCreator {}

impl CommandCreator {
    // task 1
    fn create(command: String) -> impl Fn(Position) -> Position {
        let (name, amount) = CommandCreator::parse_command(command);
        move |mut current_position| {
            match name.as_str() {
                "forward" => current_position.x = current_position.x + amount,
                "up" => current_position.z = current_position.z - amount,
                "down" => current_position.z = current_position.z + amount,
                _ => (),
            };
            current_position
        }
    }

    // task 2
    fn create_2(command: String) -> impl Fn(Position) -> Position {
        let (name, amount) = CommandCreator::parse_command(command);
        move |mut current_position| {
            match name.as_str() {
                "forward" => {
                    current_position.x = current_position.x + amount;
                    current_position.z = current_position.z + current_position.aim * amount;
                }
                "up" => current_position.aim = current_position.aim - amount,
                "down" => current_position.aim = current_position.aim + amount,
                _ => (),
            };
            current_position
        }
    }

    fn parse_command(command: String) -> (String, u32) {
        command
            .split_once(' ')
            .map(|(n, a)| (String::from(n), String::from(a)))
            .map(|(n, a)| a.parse::<u32>().ok().map(|parsed| (n, parsed)))
            .flatten()
            .unwrap_or(("forward".to_owned(), 0)) // malformed commands are turned into no-ops
    }
}

pub fn solve(path: &str) -> Position {
    solve_with_creator(path, CommandCreator::create)
}

pub fn solve_2(path: &str) -> Position {
    solve_with_creator(path, CommandCreator::create_2)
}

fn solve_with_creator<C>(path: &str, creator: impl Fn(String) -> C) -> Position
where
    C: Command,
{
    use super::utils::read_lines;

    match read_lines(path) {
        None => Position::new(),
        Some(lines) => lines
            .into_iter()
            .map(creator)
            .fold(Position::new(), |pos, cmd| cmd.run(pos)),
    }
}
