pub mod test;

pub struct Report {
    n_bits: usize,
    numbers: Vec<u16>,
}

use std::collections::HashSet;
impl Report {
    fn new(n_bits: usize) -> Self {
        Self {
            n_bits: n_bits,
            numbers: Vec::new(),
        }
    }

    fn update(mut self, line: &str) -> Self {
        if let Some(n) = u16::from_str_radix(line.trim(), 2).ok() {
            self.numbers.push(n)
        }
        self
    }

    // returns a set of bit indices where 1 is more common
    fn mcb(&self) -> HashSet<usize> {
        BitStats::new(self.n_bits, self.numbers.iter()).to_set()
    }

    fn power_consumption(&self) -> u64 {
        let mcb = self.mcb();
        self.gamma_rate(&mcb) * self.epsilon_rate(&mcb)
    }

    fn gamma_rate(&self, mcb: &HashSet<usize>) -> u64 {
        (0..self.n_bits)
            .filter(|pos| mcb.contains(pos))
            .map(|pos| u64::pow(2, (self.n_bits - 1 - pos) as u32))
            .sum()
    }

    fn epsilon_rate(&self, mcb: &HashSet<usize>) -> u64 {
        (0..self.n_bits)
            .filter(|pos| !mcb.contains(pos))
            .map(|pos| u64::pow(2, (self.n_bits - 1 - pos) as u32))
            .sum()
    }
}

struct BitStats {
    bits: Vec<(u16, u16)>,
}

impl BitStats {
    fn new<'a, I>(n_bits: usize, numbers: I) -> Self
    where
        I: Iterator<Item = &'a u16>,
    {
        Self {
            bits: numbers
                .map(|n| {
                    (0..n_bits)
                        .map(move |idx| (n / u16::pow(2, (n_bits - 1 - idx) as u32) % 2) as u8)
                })
                .fold(BitStats::init_acc(n_bits), |acc, digits| {
                    digits.enumerate().fold(acc, |mut bit_stats, (idx, digit)| {
                        let (zeros, ones) = bit_stats[idx];
                        if digit == 0 {
                            bit_stats[idx] = (zeros + 1, ones)
                        } else {
                            bit_stats[idx] = (zeros, ones + 1)
                        }
                        bit_stats
                    })
                }),
        }
    }

    fn init_acc(n_bits: usize) -> Vec<(u16, u16)> {
        let mut vec = Vec::new();
        for _ in 0..n_bits {
            vec.push((0, 0));
        }
        vec
    }

    fn to_set(&self) -> HashSet<usize> {
        self.bits
            .iter()
            .enumerate()
            .filter(|(_, (zeros, ones))| zeros <= ones)
            .map(|(idx, _)| idx)
            .collect()
    }
}

pub fn solve(path: &str) -> u64 {
    use super::utils::read_lines;

    match read_lines(path) {
        None => 0,
        Some(lines) => lines
            .iter()
            .fold(Report::new(lines[0].trim().len()), |report, line| {
                report.update(line)
            })
            .power_consumption(),
    }
}
