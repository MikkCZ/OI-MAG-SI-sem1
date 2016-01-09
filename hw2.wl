(* ::Package:: *)

(* 
   Implementation of BOS state. 
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


(*Big-Step Operational Semantics*)
Clear[oneStep];
(*2*) oneStep[\[Sigma]_,CBlock[{s___}]]:=oneStep[\[Sigma],{s}];

(*3*) oneStep[s_,{}]:={s,Null};

(*4*) oneStep[s_,{c_,p___}]:=oneStep[oneStep[s,c][[1]],{p}];

(*5*) oneStep[s_,CDeclare[type_,var_]]:={put[s,ToString[var],Undefined],Null};

(*6*) oneStep[s_,CWhile[e_,{stm___}]]:=Module[
    {tmp},
    tmp=oneStep[s,e];
    If[tmp[[2]]==0,
        {tmp[[1]], Null},
        oneStep[ tmp[[1]], {{stm},CWhile[e, {stm}]} ]
    ]
];

(*8*) oneStep[s_,k_Integer]:={s,k}
oneStep[s_,k_Real]:={s,k}
(*9*) oneStep[s_,var_]:={s,get[s,ToString[var]]}
oneStep[s_,e_]:={s,e}
oneStep[{},e_Symbol]:={{},Null}
oneStep[s_,e_Symbol]:={s,get[s,ToString[e]]}
(*10*)oneStep[s_,CAssign[var_,e_]]:=Module[ {tmp}, tmp=oneStep[s,e]; {put[tmp[[1]],ToString[var],tmp[[2]]] , tmp[[2]]} ]
(*11*)oneStep[s_,COperator[Minus,e_]]:=Module[ {tmp}, tmp=oneStep[s,e]; {tmp[[1]],-tmp[[2]]} ];
(*12,13*)
      oneStep[s_,COperator[Not,e_]]:=Module[ {tmp}, tmp=oneStep[s,e]; If[tmp[[2]]==0, tmp[[2]]=1, tmp[[2]]=0]; tmp ];
(*14*)oneStep[s_,COperator[Plus,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2]; {tmp2[[1]],tmp[[2]]+tmp2[[2]]}
];
(*15*)oneStep[s_,COperator[Subtract,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2]; {tmp2[[1]],tmp[[2]]-tmp2[[2]]}
];
(*16*)oneStep[s_,COperator[Times,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2]; {tmp2[[1]],tmp[[2]]*tmp2[[2]]}
];
(*17,18*)
      oneStep[s_,COperator[Divide,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
If[IntegerQ[e1] && IntegerQ[e2],
    If[tmp2[[2]]=!=0,
        {tmp2[[1]],IntegerPart[N[Divide[tmp[[2]],tmp2[[2]]]]]},
        {{},$Failed}
    ]
,
    If[tmp2[[2]]=!=0.,
        {tmp2[[1]],Divide[tmp[[2]],tmp2[[2]]]},
        {{},$Failed}
    ]
]
];
(*19,20*)
      oneStep[s_,COperator[Greater,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]>tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[GreaterEqual,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]>=tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[Less,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]<tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[LessEqual,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]<=tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[Equal,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]==tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[Unequal,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]=!=tmp2[[2]],
        {tmp2[[1]],1},
        {tmp2[[1]],0}
    ]
];
(*19,20*)
      oneStep[s_,COperator[And,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]==0 || tmp[[2]]==0.,
        {tmp[[1]],0},
        If[tmp2[[2]]=!=0 && tmp2[[2]]=!=0.,
            {tmp2[[1]],1},
            {tmp2[[1]],0}
        ]
    ]
];
(*19,20*)
      oneStep[s_,COperator[Or,{e1_,e2_}]]:=Module[
    {tmp,tmp2}, tmp=oneStep[s,e1]; tmp2=oneStep[tmp[[1]],e2];
    If[tmp[[2]]=!=0 && tmp[[2]]=!=0.,
        {tmp[[1]],1},
        If[tmp2[[2]]=!=0 && tmp2[[2]]=!=0.,
            {tmp2[[1]],1},
            {tmp2[[1]],0}
        ]
    ]
];
(*
oneStep[{},4]
oneStep[{},3.14]
oneStep[{},x]
oneStep[{{"x", 10}},x]
oneStep[{},{}]
oneStep[{},CDeclare[int, x]]
oneStep[{},{CDeclare[int, x]}]
oneStep[{},{CDeclare[int, x], CDeclare[int, x]}]
oneStep[{},{CDeclare[int, x], CDeclare[double, x]}]
oneStep[{},{CDeclare[int, x], x}]
oneStep[{},{CDeclare[double, x], x}]
oneStep[{},{CDeclare[int, x], CAssign[x, 1]}]
oneStep[{},{CDeclare[int, x], CAssign[x, 1.2]}]
oneStep[{},{CDeclare[double, x], CAssign[x, 1]}]
oneStep[{},{CDeclare[double, x], CAssign[x, 1.2]}]
oneStep[{},CAssign[x, 42]]
oneStep[{},{CAssign[x, 42]}]
oneStep[{},COperator[Minus, 42]]
oneStep[{},COperator[Minus, 3.14]]
oneStep[{},COperator[Not, 42]]
oneStep[{},COperator[Not, 3.14]]
oneStep[{},COperator[Not, 0]]
oneStep[{},COperator[Not, 1]]
oneStep[{},COperator[Plus, {2, 14}]]
oneStep[{},COperator[Plus, {13, 8}]]
oneStep[{},COperator[Plus, {0, 0}]]
oneStep[{},COperator[Plus, {4.7, 20}]]
oneStep[{},COperator[Plus, {10, 12.6}]]
oneStep[{},COperator[Plus, {1.4, 19.}]]
oneStep[{},COperator[Plus, {0., 0.}]]
oneStep[{},COperator[Plus, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Plus, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Subtract, {4, 18}]]
oneStep[{},COperator[Subtract, {12, 9}]]
oneStep[{},COperator[Subtract, {0, 0}]]
oneStep[{},COperator[Subtract, {6., 20}]]
oneStep[{},COperator[Subtract, {10, 14.7}]]
oneStep[{},COperator[Subtract, {6.8, 13.5}]]
oneStep[{},COperator[Subtract, {0., 0.}]]
oneStep[{},COperator[Subtract, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Subtract, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Times, {10, 12}]]
oneStep[{},COperator[Times, {17, 5}]]
oneStep[{},COperator[Times, {0, 0}]]
oneStep[{},COperator[Times, {2.5, 20}]]
oneStep[{},COperator[Times, {10, 12.7}]]
oneStep[{},COperator[Times, {5.9, 17.5}]]
oneStep[{},COperator[Times, {0., 0.}]]
oneStep[{},COperator[Times, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Times, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Divide, {7, 17}]]
oneStep[{},COperator[Divide, {13, 7}]]
oneStep[{},COperator[Divide, {0, 0}]]
oneStep[{},COperator[Divide, {4.2, 20}]]
oneStep[{},COperator[Divide, {10, 14.8}]]
oneStep[{},COperator[Divide, {6.8, 11.8}]]
oneStep[{},COperator[Divide, {0., 0.}]]
oneStep[{},COperator[Divide, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Divide, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Greater, {3, 18}]]
oneStep[{},COperator[Greater, {19, 9}]]
oneStep[{},COperator[Greater, {0, 0}]]
oneStep[{},COperator[Greater, {7.9, 20}]]
oneStep[{},COperator[Greater, {10, 18.7}]]
oneStep[{},COperator[Greater, {5.9, 18.}]]
oneStep[{},COperator[Greater, {0., 0.}]]
oneStep[{},COperator[Greater, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Greater, {1, CAssign[x, 1]}]]
oneStep[{},COperator[GreaterEqual, {1, 15}]]
oneStep[{},COperator[GreaterEqual, {19, 7}]]
oneStep[{},COperator[GreaterEqual, {0, 0}]]
oneStep[{},COperator[GreaterEqual, {1.7, 20}]]
oneStep[{},COperator[GreaterEqual, {10, 13.5}]]
oneStep[{},COperator[GreaterEqual, {1.5, 12.3}]]
oneStep[{},COperator[GreaterEqual, {0., 0.}]]
oneStep[{},COperator[GreaterEqual, {0, CAssign[x, 1]}]]
oneStep[{},COperator[GreaterEqual, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Less, {6, 13}]]
oneStep[{},COperator[Less, {19, 4}]]
oneStep[{},COperator[Less, {0, 0}]]
oneStep[{},COperator[Less, {2.3, 20}]]
oneStep[{},COperator[Less, {10, 17.5}]]
oneStep[{},COperator[Less, {8.6, 11.7}]]
oneStep[{},COperator[Less, {0., 0.}]]
oneStep[{},COperator[Less, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Less, {1, CAssign[x, 1]}]]
oneStep[{},COperator[LessEqual, {8, 18}]]
oneStep[{},COperator[LessEqual, {18, 9}]]
oneStep[{},COperator[LessEqual, {0, 0}]]
oneStep[{},COperator[LessEqual, {8.3, 20}]]
oneStep[{},COperator[LessEqual, {10, 11.3}]]
oneStep[{},COperator[LessEqual, {3.3, 15.3}]]
oneStep[{},COperator[LessEqual, {0., 0.}]]
oneStep[{},COperator[LessEqual, {0, CAssign[x, 1]}]]
oneStep[{},COperator[LessEqual, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Equal, {5, 16}]]
oneStep[{},COperator[Equal, {12, 2}]]
oneStep[{},COperator[Equal, {0, 0}]]
oneStep[{},COperator[Equal, {3.4, 20}]]
oneStep[{},COperator[Equal, {10, 13.7}]]
oneStep[{},COperator[Equal, {1.9, 17.1}]]
oneStep[{},COperator[Equal, {0., 0.}]]
oneStep[{},COperator[Equal, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Equal, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Unequal, {4, 18}]]
oneStep[{},COperator[Unequal, {16, 5}]]
oneStep[{},COperator[Unequal, {0, 0}]]
oneStep[{},COperator[Unequal, {7.6, 20}]]
oneStep[{},COperator[Unequal, {10, 15.5}]]
oneStep[{},COperator[Unequal, {1.5, 13.7}]]
oneStep[{},COperator[Unequal, {0., 0.}]]
oneStep[{},COperator[Unequal, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Unequal, {1, CAssign[x, 1]}]]
oneStep[{},COperator[And, {9, 12}]]
oneStep[{},COperator[And, {15, 4}]]
oneStep[{},COperator[And, {0, 0}]]
oneStep[{},COperator[And, {5.7, 20}]]
oneStep[{},COperator[And, {10, 13.7}]]
oneStep[{},COperator[And, {7., 15.2}]]
oneStep[{},COperator[And, {0., 0.}]]
oneStep[{},COperator[And, {0, CAssign[x, 1]}]]
oneStep[{},COperator[And, {1, CAssign[x, 1]}]]
oneStep[{},COperator[Or, {9, 17}]]
oneStep[{},COperator[Or, {15, 2}]]
oneStep[{},COperator[Or, {0, 0}]]
oneStep[{},COperator[Or, {1., 20}]]
oneStep[{},COperator[Or, {10, 16.1}]]
oneStep[{},COperator[Or, {8.3, 11.6}]]
oneStep[{},COperator[Or, {0., 0.}]]
oneStep[{},COperator[Or, {0, CAssign[x, 1]}]]
oneStep[{},COperator[Or, {1, CAssign[x, 1]}]]
oneStep[{},CBlock[{}]]
oneStep[{},CBlock[{CDeclare[int, i]}]]
oneStep[{},CBlock[{CDeclare[int, i], CAssign[i, -5]}]]
oneStep[{},CBlock[{CDeclare[int, i], CAssign[i, 0], CWhile[COperator[Less, {i, 10}], {CAssign[i, COperator[Plus, {i, 1}]]}]}]]
oneStep[{},CBlock[{CDeclare[int, i], CDeclare[int, x], CAssign[i, 1], CAssign[x, 1], CWhile[COperator[Less, {i, 10}], {CAssign[i, COperator[Plus, {i, 1}]], CAssign[x, COperator[Times, {x, i}]]}]}]]
*)



