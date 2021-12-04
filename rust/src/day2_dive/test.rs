#![cfg(test)]

const PATH: &str = "puzzles/day2/input";

#[test]
pub fn day2_task1() {
    println!(
        "[Day2 Task 1] Final position product: {}",
        super::solve(PATH).product()
    )
}

#[test]
pub fn day2_task2() {
    println!(
        "[Day2 Task 2] Final position product: {}",
        super::solve_2(PATH).product()
    )
}
