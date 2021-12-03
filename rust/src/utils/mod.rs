use std::fs::File;
use std::io::{BufRead, BufReader};
use std::path::Path;

pub fn read_lines<PATH>(path: PATH) -> Option<Vec<String>>
where
    PATH: AsRef<Path>,
{
    File::open(path)
        .ok() // file open result to option
        .map(|file| BufReader::new(file).lines()) // map file to iterator of line results
        .map(|lines| lines.flat_map(|line| line.ok()).collect()) // map results to options, filter none's, collect to vector
}
