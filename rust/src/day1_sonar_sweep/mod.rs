pub mod test;

use super::utils::read_lines;

pub fn solve(path: &str) -> usize {
    match read_lines(path) {
        None => 0,
        Some(lines) => count(numbers(lines)),
    }
}

pub fn solve_2(path: &str) -> usize {
    match read_lines(path) {
        None => 0,
        Some(lines) => {
            let numbers = numbers(lines);

            let windowed = numbers
                .iter()
                .zip(numbers.iter().skip(1))
                .zip(numbers.iter().skip(2))
                .map(|((a, b), c)| a + b + c)
                .collect();

            count(windowed)
        }
    }
}

fn numbers(lines: Vec<String>) -> Vec<u16> {
    lines.iter().flat_map(|line| line.parse().ok()).collect()
}

fn count(numbers: Vec<u16>) -> usize {
    numbers
        .iter()
        .zip(numbers.iter().skip(1))
        .filter(|(a, b)| a < b)
        .count()
}
