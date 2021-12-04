pub mod test;

pub struct Position {
    x: u32,
    z: u32,
}

impl Position {
    fn new() -> Self {
        Self { x: 0, z: 0 }
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

// functions on position implement the command trait (~ functional interface)
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
    fn create<'a>(command: &'a str) -> impl Fn(Position) -> Position + 'a {
        let (name, amount) = CommandCreator::parse_command(command);
        move |mut current_position| {
            match name {
                "forward" => current_position.x = current_position.x + amount,
                "up" => current_position.z = current_position.z - amount,
                "down" => current_position.z = current_position.z + amount,
                _ => (),
            };
            current_position
        }
    }

    fn parse_command(command: &str) -> (&str, u32) {
        command
            .split_once(' ')
            .map(|(n, a)| a.parse::<u32>().ok().map(|parsed| (n, parsed)))
            .flatten()
            .unwrap_or(("forward", 0)) // malformed commands are turned into no-ops
    }
}

pub fn solve(path: &str) -> Position {
    use super::utils::read_lines;

    match read_lines(path) {
        None => Position::new(),
        Some(lines) => lines
            .iter()
            .map(|line| CommandCreator::create(line))
            .fold(Position::new(), |pos, cmd| cmd(pos)),
    }
}
