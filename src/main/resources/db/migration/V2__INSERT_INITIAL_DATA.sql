insert into sport (id, name, created_at)
values (1, 'NFL', current_timestamp);

insert into team (id, sport_id, name, created_at)
values (1, 1, 'Tampa Bay Buccaneers', current_timestamp);


insert into player (id, player_no, team_id, player_name, created_at)
values (1, 13, 1, 'Evans, Mike', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (2, 1, 1, 'Darden, Jaelon', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (3, 10, 1, 'Miller, Scott', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (4, 18, 1, 'Johnson, Tyler', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (5, 16, 1, 'Perriman, Breshad', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (6, 15, 1, 'Grayson, Cyril', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (7, 76, 1, 'Smith, Donovan', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (8, 72, 1, 'WELLS, JOSH', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (9, 74, 1, 'Marpet, Ali', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (10, 60, 1, 'Leverett, Nick', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (11, 66, 1, 'JENSEN, RYAN', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (12, 70, 1, 'Hainsey, Robert', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (13, 65, 1, 'Cappa, Alex', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (14, 64, 1, 'Stinnie, Aaron', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (15, 78, 1, 'Wirfs, Tristan', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (16, 80, 1, 'Howard, OJ', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (17, 84, 1, 'BRATE, CAMERON', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (18, 87, 1, 'GRONKOWSKI, ROB', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (19, 12, 1, 'BRADY, TOM', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (20, 11, 1, 'GABBERT, BLAINE', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (21, 2, 1, 'Trask, Kyle', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (22, 7, 1, 'Fournette, Leonard', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (23, 27, 1, 'Jones II, Ronald', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (24, 21, 1, 'Vaughn, Ke''Shawn', current_timestamp);
insert into player (id, player_no, team_id, player_name, created_at)
values (25, 25, 1, 'BERNARD, GIOVANI', current_timestamp);



insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (1, 1, 'LWR', 'LWR', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (2, 1, 'RWR', 'RWR', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (3, 1, 'LT', 'LT', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (4, 1, 'LG', 'LG', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (5, 1, 'C', 'C', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (6, 1, 'RG', 'RG', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (7, 1, 'RT', 'RT', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (8, 1, 'TE1', 'TE', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (9, 1, 'TE2', 'TE', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (10, 1, 'QB', 'QB', 'offense', current_timestamp);
insert into public.position (id, sport_id, position_name, real_position_name, position_catergory, created_at)
values (11, 1, 'RB', 'RB', 'offense', current_timestamp);

-- player_position data

--LWR
insert into player_position (player_id, position_id, position_depth, created_at)
values (1, 1, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (2, 1, 2, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (3, 1, 3, current_timestamp);

--RWR
insert into player_position (player_id, position_id, position_depth, created_at)
values (4, 2, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (5, 2, 2, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (6, 2, 3, current_timestamp);

--LT
insert into player_position (player_id, position_id, position_depth, created_at)
values (7, 3, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (8, 3, 2, current_timestamp);

--LG
insert into player_position (player_id, position_id, position_depth, created_at)
values (9, 4, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (10, 4, 2, current_timestamp);

--RT
insert into player_position (player_id, position_id, position_depth, created_at)
values (14, 7, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (15, 7, 2, current_timestamp);

--TE
insert into player_position (player_id, position_id, position_depth, created_at)
values (16, 8, 1, current_timestamp);
insert into player_position (player_id, position_id, position_depth, created_at)
values (17, 8, 2, current_timestamp);

--TE
insert into player_position (player_id, position_id, position_depth, created_at)
values (18, 9, 1, current_timestamp);