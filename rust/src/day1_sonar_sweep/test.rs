#![cfg(test)]

const PATH: &str = "puzzles/day1/input";

#[test]
pub fn day1_task1() {
    println!(
        "[Day1 Task 1] Number of depth increases: {}",
        super::solve(PATH)
    )
}

#[test]
pub fn day1_task2() {
    println!(
        "[Day1 Task 2] Number of depth increases: {}",
        super::solve_2(PATH)
    )
}
