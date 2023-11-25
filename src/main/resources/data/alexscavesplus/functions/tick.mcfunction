##Made By WitherBean

#Ajoltodon Spawning
execute as @e[type=frog] run scoreboard players add @s them_spawn_timer 1
execute as @a if score @s themspawn matches 10 run scoreboard players reset @s themspawn
execute as @e[type=frog,scores={them_spawn_timer=1}] at @s if score @p themspawn matches 1 run tag @s add them
execute as @e[tag=them] at @s if score @s them_spawn_timer matches 2 run summon alexscavesplus:ajoltodon ~ ~ ~
execute as @a run scoreboard players add @s themspawn 1

#Lacandrae Spawning
execute as @e[type=alexscavesplus:ajoltodon] run scoreboard players add @s them_spawn_timer 1
execute as @a if score @s themspawn matches 10 run scoreboard players reset @s themspawn
execute as @e[type=alexscavesplus:ajoltodon,scores={them_spawn_timer=1}] at @s if score @p themspawn matches 1 run tag @s add them
execute as @e[tag=them] at @s if score @s them_spawn_timer matches 2 run summon alexscavesplus:lacandrae ~ ~ ~