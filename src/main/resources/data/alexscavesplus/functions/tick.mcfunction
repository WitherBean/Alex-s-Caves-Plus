##Made By WitherBean

#Ajoltodon Spawning
execute as @e[type=frog] run scoreboard players add @s them_spawn_timer 1
execute as @a if score @s themspawn matches 10 run scoreboard players reset @s themspawn
execute as @e[type=frog,scores={them_spawn_timer=1}] at @s if score @p themspawn matches 1 run tag @s add thema
execute as @e[tag=thema] at @s if score @s them_spawn_timer matches 2 run summon alexscavesplus:ajolotodon ~ ~ ~
execute as @a run scoreboard players add @s themspawn 1

#Vortex Spawning
execute as @e[type=enderman] run scoreboard players add @s v_spawn_timer 1
execute as @a if score @s vspawn matches 10 run scoreboard players reset @s vspawn
execute as @e[type=enderman,scores={v_spawn_timer=1}] at @s if score @p vspawn matches 1 run tag @s add themv
execute as @e[tag=themv] at @s if score @s v_spawn_timer matches 2 run summon alexscavesplus:vortex ~ ~ ~
execute as @a run scoreboard players add @s vpawn 1