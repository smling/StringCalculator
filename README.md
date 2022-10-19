# String Calculator # 

## Assumptions / Constraints ##
1. Previous unit tests should be pass;
1. For item(2) handling an unknown numbers, it will skip non-number in parameter `numbers`;
   1. Example: `1,2,a` => 3
2. For item(8) and (9) multiple delimiters, delimiters with `[]` will define as multiple delimiters, if not, will assume it is single delimiter;
   1. Example: `//[|||]\n1|||2|||3` => multiple delimiters detected with 1 item (`|||`);
   2. Example: `//[|][%]\n1|2%3` =>  multiple delimiters detected with 2 items (`|`, `%`);