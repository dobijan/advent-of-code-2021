#![cfg(test)]

const PATH: &str = "puzzles/day2/input";

#[test]
pub fn day1_task1() {
    println!(
        "[Day2 Task 1] Final position product: {}",
        super::solve(PATH).product()
    )
}
