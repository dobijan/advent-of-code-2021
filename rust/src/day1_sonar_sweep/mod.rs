pub mod test;

use super::utils::read_lines;
use std::path::Path;

pub fn solve<PATH>(path: PATH) -> usize
where
    PATH: AsRef<Path>,
{
    read_lines(path)
        .map(|lines| {
            lines
                .iter()
                .flat_map(|line| line.parse().ok())
                .collect::<Vec<u16>>()
        })
        .map(|numbers| {
            numbers
                .iter()
                .zip(numbers.iter().skip(1))
                .filter(|(a, b)| a < b)
                .count()
        })
        .unwrap_or(0)
}
