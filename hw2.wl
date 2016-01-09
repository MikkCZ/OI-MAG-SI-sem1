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
oneStep[{},s_String]:={{},Null}
oneStep[s_,e_Symbol]:={s,get[s,ToString[e]]}
oneStep[s_,e_String]:={s,get[s,e]}
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
oneStep[{},"x"]
oneStep[{{"x", 10}},"x"]
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


(*Typing System*)
Clear[typeOf];
(*37*)typeOf[g_,n_Integer]:="int"
(*38*)typeOf[g_,d_Real]:="double"
(*39*)typeOf[g_,CBlock[{stm___}]]:=typeOf[g,{stm}];

(*40*)typeOf[g_,{}]:="command";
typeOf[g_,s_Symbol] /; get[g,ToString[s]]=!=Null :="command";
typeOf[g_,s_String] /; get[g,s]=!=Null :="command";

(*41*)typeOf[g_,{n_,stm___}] :=If[typeOf[g,n]=!=$Failed, typeOf[g,{stm}], $Failed];

(*42*)typeOf[g_,CAssign[var_,e_]] /; get[g,ToString[var]]=="double"&&typeOf[g,e]=="int":="command";
(*43*)typeOf[g_,CAssign[var_,e_]] /; get[g,ToString[var]]==typeOf[g,e]:="command";
(*44*)typeOf[g_,{a:CAssign[var_,e_],stm___}] /; typeOf[g,a]=!=$Failed :=typeOf[g,{stm}];
(*45*)typeOf[g_,CDeclare[type_,var_]] /; get[g,ToString[var]]==Null :="command"
(*46*)typeOf[g_,{CDeclare[type_,var_]}] := typeOf[g,CDeclare[type,var]];
(*46*)typeOf[g_,{CDeclare[type_,var_],stm___}] /; typeOf[g,CDeclare[type,var]]=="command" := typeOf[put[g,ToString[var],ToString[type]],{stm}]
(*47*)typeOf[g_,CWhile[e_,s_]] := If[
    typeOf[g,e]=!=$Failed && typeOf[g,s]=="command",
    "command",
    $Failed
];
(*48*)(*typeOf[g_,{CWhile[e_,s_],{stm___}}] := If[
    typeOf[g,CWhile[e,s]]\[Equal]"command" && typeOf[g,{stm}]\[Equal]"command",
    "command",
    $Failed
];*)

notList[s_List]:=False;
notList[_]:=True;
(*51*)typeOf[g_,COperator[unaryOp_,e_]] /; notList[e] := typeOf[g,e];

typeOf[g_,COperator[binaryOp_,{e_Symbol,f_}]] /; get[g,ToString[e]]==typeOf[g,f] := typeOf[g,f];
typeOf[g_,COperator[binaryOp_,{e_String,f_}]] /; get[g,e]==typeOf[g,f] := typeOf[g,f];
(*53*)typeOf[g_,COperator[binaryOp_,{e_,f_}]] /; typeOf[g,e]=="double" && typeOf[g,f]=="int" := "double";
(*54*)typeOf[g_,COperator[binaryOp_,{e_,f_}]] /; typeOf[g,e]=="int" && typeOf[g,f]=="double" := "double";
(*55*)typeOf[g_,COperator[binaryOp_,{e_,f_}]] /; typeOf[g,e]==typeOf[g,f] := typeOf[g,e];

typeOf[_,_]:=$Failed;

(*typeOf[{},-2]
typeOf[{},3.14]
typeOf[{},x]
typeOf[{},x]
typeOf[{},{}]
typeOf[{},CDeclare[int, x]]
typeOf[{},{CDeclare[int, x]}]
typeOf[{},{CDeclare[int, x], CDeclare[int, x]}]
typeOf[{},{CDeclare[int, x], CDeclare[double, x]}]
typeOf[{},{CDeclare[int, x], x}]
typeOf[{},{CDeclare[double, x], x}]
typeOf[{},{CDeclare[int, x], CAssign[x, 1]}]
typeOf[{},{CDeclare[int, x], CAssign[x, 1.2]}]
typeOf[{},{CDeclare[double, x], CAssign[x, 1]}]
typeOf[{},{CDeclare[double, x], CAssign[x, 1.2]}]
typeOf[{},CAssign[x, 42]]
typeOf[{},{CAssign[x, 42]}]
typeOf[{},COperator[Minus, 42]]
typeOf[{},COperator[Minus, 3.14]]
typeOf[{},COperator[Not, 42]]
typeOf[{},COperator[Not, 3.14]]
typeOf[{},COperator[Not, 0]]
typeOf[{},COperator[Not, 1]]
typeOf[{},COperator[Plus, {10, 14}]]
typeOf[{},COperator[Plus, {11, 10}]]
typeOf[{},COperator[Plus, {0, 0}]]
typeOf[{},COperator[Plus, {7.3, 20}]]
typeOf[{},COperator[Plus, {10, 18.8}]]
typeOf[{},COperator[Plus, {7.3, 18.3}]]
typeOf[{},COperator[Plus, {0., 0.}]]
typeOf[{},COperator[Plus, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Plus, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Subtract, {10, 18}]]
typeOf[{},COperator[Subtract, {15, 1}]]
typeOf[{},COperator[Subtract, {0, 0}]]
typeOf[{},COperator[Subtract, {6.3, 20}]]
typeOf[{},COperator[Subtract, {10, 19.4}]]
typeOf[{},COperator[Subtract, {6.3, 12.2}]]
typeOf[{},COperator[Subtract, {0., 0.}]]
typeOf[{},COperator[Subtract, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Subtract, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Times, {10, 12}]]
typeOf[{},COperator[Times, {13, 6}]]
typeOf[{},COperator[Times, {0, 0}]]
typeOf[{},COperator[Times, {5.6, 20}]]
typeOf[{},COperator[Times, {10, 18.}]]
typeOf[{},COperator[Times, {8.7, 14.5}]]
typeOf[{},COperator[Times, {0., 0.}]]
typeOf[{},COperator[Times, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Times, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Divide, {3, 13}]]
typeOf[{},COperator[Divide, {19, 5}]]
typeOf[{},COperator[Divide, {0, 0}]]
typeOf[{},COperator[Divide, {7.7, 20}]]
typeOf[{},COperator[Divide, {10, 12.6}]]
typeOf[{},COperator[Divide, {9.8, 14.9}]]
typeOf[{},COperator[Divide, {0., 0.}]]
typeOf[{},COperator[Divide, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Divide, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Greater, {8, 13}]]
typeOf[{},COperator[Greater, {16, 4}]]
typeOf[{},COperator[Greater, {0, 0}]]
typeOf[{},COperator[Greater, {4.9, 20}]]
typeOf[{},COperator[Greater, {10, 11.9}]]
typeOf[{},COperator[Greater, {7.2, 13.1}]]
typeOf[{},COperator[Greater, {0., 0.}]]
typeOf[{},COperator[Greater, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Greater, {1, CAssign[x, 1]}]]
typeOf[{},COperator[GreaterEqual, {4, 17}]]
typeOf[{},COperator[GreaterEqual, {18, 2}]]
typeOf[{},COperator[GreaterEqual, {0, 0}]]
typeOf[{},COperator[GreaterEqual, {8.5, 20}]]
typeOf[{},COperator[GreaterEqual, {10, 12.4}]]
typeOf[{},COperator[GreaterEqual, {7.1, 17.8}]]
typeOf[{},COperator[GreaterEqual, {0., 0.}]]
typeOf[{},COperator[GreaterEqual, {0, CAssign[x, 1]}]]
typeOf[{},COperator[GreaterEqual, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Less, {2, 20}]]
typeOf[{},COperator[Less, {20, 5}]]
typeOf[{},COperator[Less, {0, 0}]]
typeOf[{},COperator[Less, {6.2, 20}]]
typeOf[{},COperator[Less, {10, 11.2}]]
typeOf[{},COperator[Less, {1., 13.1}]]
typeOf[{},COperator[Less, {0., 0.}]]
typeOf[{},COperator[Less, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Less, {1, CAssign[x, 1]}]]
typeOf[{},COperator[LessEqual, {5, 18}]]
typeOf[{},COperator[LessEqual, {15, 9}]]
typeOf[{},COperator[LessEqual, {0, 0}]]
typeOf[{},COperator[LessEqual, {1.1, 20}]]
typeOf[{},COperator[LessEqual, {10, 20.}]]
typeOf[{},COperator[LessEqual, {1., 13.6}]]
typeOf[{},COperator[LessEqual, {0., 0.}]]
typeOf[{},COperator[LessEqual, {0, CAssign[x, 1]}]]
typeOf[{},COperator[LessEqual, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Equal, {5, 19}]]
typeOf[{},COperator[Equal, {12, 2}]]
typeOf[{},COperator[Equal, {0, 0}]]
typeOf[{},COperator[Equal, {6.8, 20}]]
typeOf[{},COperator[Equal, {10, 14.8}]]
typeOf[{},COperator[Equal, {7.4, 12.8}]]
typeOf[{},COperator[Equal, {0., 0.}]]
typeOf[{},COperator[Equal, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Equal, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Unequal, {3, 15}]]
typeOf[{},COperator[Unequal, {15, 6}]]
typeOf[{},COperator[Unequal, {0, 0}]]
typeOf[{},COperator[Unequal, {5.3, 20}]]
typeOf[{},COperator[Unequal, {10, 18.5}]]
typeOf[{},COperator[Unequal, {3., 17.5}]]
typeOf[{},COperator[Unequal, {0., 0.}]]
typeOf[{},COperator[Unequal, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Unequal, {1, CAssign[x, 1]}]]
typeOf[{},COperator[And, {2, 20}]]
typeOf[{},COperator[And, {16, 4}]]
typeOf[{},COperator[And, {0, 0}]]
typeOf[{},COperator[And, {8.6, 20}]]
typeOf[{},COperator[And, {10, 18.3}]]
typeOf[{},COperator[And, {9.7, 12.4}]]
typeOf[{},COperator[And, {0., 0.}]]
typeOf[{},COperator[And, {0, CAssign[x, 1]}]]
typeOf[{},COperator[And, {1, CAssign[x, 1]}]]
typeOf[{},COperator[Or, {5, 11}]]
typeOf[{},COperator[Or, {17, 9}]]
typeOf[{},COperator[Or, {0, 0}]]
typeOf[{},COperator[Or, {1.1, 20}]]
typeOf[{},COperator[Or, {10, 11.}]]
typeOf[{},COperator[Or, {1.7, 20.}]]
typeOf[{},COperator[Or, {0., 0.}]]
typeOf[{},COperator[Or, {0, CAssign[x, 1]}]]
typeOf[{},COperator[Or, {1, CAssign[x, 1]}]]
typeOf[{},CBlock[{}]]
typeOf[{},CBlock[{CDeclare[int, i]}]]
typeOf[{},CBlock[{CDeclare[int, i], CAssign[i, 10]}]]
typeOf[{},CBlock[{CDeclare[int, i], CAssign[i, 0], CWhile[COperator[Less, {i, 10}], {CAssign[i, COperator[Plus, {i, 1}]]}]}]]
typeOf[{},CBlock[{CDeclare[int, i], CDeclare[int, x], CAssign[i, 1], CAssign[x, 1], CWhile[COperator[Less, {i, 10}], {CAssign[i, COperator[Plus, {i, 1}]], CAssign[x, COperator[Times, {x, i}]]}]}]]
*)



