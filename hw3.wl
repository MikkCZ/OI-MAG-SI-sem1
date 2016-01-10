(* ::Package:: *)

(* 
   Implementation of denotational semantics state. 
   The state is represented by the list of pairs { varName, value }.
*)
initState[] :=
    {}; 

put[state_, varName_String, value_] :=
    Sow[
    Append[DeleteCases[state, {varName, _}], {varName, value}]
    ]; 

get[state_, varName_String] :=
    If[ MemberQ[state, {varName, _}],
        Last[First[Cases[state, {varName, _}]]],
        Null
    ]; 


Clear[DE];
(* Value rules *)
(*2*)DE[n_?NumberQ] := Function[env,n];
(*3*)DE[true] := Function[env,True];
(*4*)DE[false] := Function[env,False];
(*5*)DE[v_String] := v;

(* Statement rules *)
DE[assign[varName_,e_]] :=DE[assign][varName,DE[e][#1],#1]&;
DE[assign][v_,e_,env_] := put[env,v,e];
DE[compose[stm_,prg_]] := DE[compose][DE[stm],DE[prg]];
DE[compose] := Function[{s,p},Function[{env},p[s[env]]]];
DE[epsilon] := Function[{env},env];
DE[while[b_,c_]] :=DE[while][b,c,#1]&;
DE[while][if_,body_,env_] := If[DE[if][env],
DE[while[if,body]][DE[body][env]],
env
];

(* Expression rules *)
DE[value[varName_]] := DE[value][varName,#1]&;
DE[value][v_,env_] := get[env,v];
DE[binop_[e1_,e2_]] :=DE[binop][DE[e1][#1],DE[e2][#1]]&;
(*16*)DE[plus]:=Function[{e1,e2},e1+e2];
(*17*)DE[less]:=Function[{e1,e2},e1<e2];
(*18*)DE[equal]:=Function[{e1,e2},e1==e2];
(*19*)DE[or]:=Function[{e1,e2},e1||e2];
(*20*)DE[and]:=Function[{e1,e2},e1&&e2];

(*DE[-1][{}]
DE[true][{}]
DE[false][{}]
DE[and[false, false]][{}]
DE[and[false, true]][{}]
DE[and[true, false]][{}]
DE[and[true, true]][{}]
DE[or[false, false]][{}]
DE[or[false, true]][{}]
DE[or[true, false]][{}]
DE[or[true, true]][{}]
DE[plus[-7, -7]][{}]
DE[less[-1, 1]][{}]
DE[less[7, -4]][{}]
DE[less[0, 0]][{}]
DE[equal[-6, -3]][{}]
DE[equal[0, 0]][{}]
DE[assign["x", -7]][{}]
DE[value["x"]][{{"x", -3}}]
DE[compose[assign["x", 10], value["x"]]][{}]
DE[compose[assign["x", 5], epsilon]][{}]
DE[compose[assign["x", 0], compose[assign["x", plus[value["x"], 1]], epsilon]]][{}]
DE[while[false, epsilon]][{{"x", 8}}]
DE[while[less[value["x"], 10], assign["x", plus[value["x"], 1]]]][{{"x", 2}}]
DE[while[less[value["i"], 7], compose[assign["x", plus[value["x"], value["x"]]], assign["i", plus[value["i"], 1]]]]][{{"i", 0}, {"x", 1}}]
*)



