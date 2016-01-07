(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0}; (*2*)

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1}; (*3*)

(* Addition - "plus" *)
(* Your code here... *)
oneStepRewrite[plus[0,f_]]:={f}; (*11*)
oneStepRewrite[plus[e_,0]]:={e}; (*12*)
oneStepRewrite[plus[n_Integer,x]] := {};
oneStepRewrite[plus[x,m_Integer]] := {};
oneStepRewrite[plus[x,x]] := {};
oneStepRewrite[plus[n_Integer,m_Integer]]:={Plus[n,m]}; (*17*)
oneStepRewrite[plus[e_,m_Integer]] := plus[oneStepRewrite[e],m]; (*6*)
oneStepRewrite[plus[n_Integer,f_]] := plus[n,oneStepRewrite[f]]; (*7*)
oneStepRewrite[plus[e_,f_]] := Flatten[{
    plus[oneStepRewrite[e],f],
    plus[e,oneStepRewrite[f]]
}]; (*6+7*)

(* Multiplication - "times" *)
(* Your code here... *)
oneStepRewrite[times[1,f_]]:={f}; (*13*)
oneStepRewrite[times[e_,1]]:={e}; (*14*)
oneStepRewrite[times[0,f_]]:={0}; (*15*)
oneStepRewrite[times[e_,0]]:={0}; (*16*)
oneStepRewrite[times[n_Integer,x]] := {};
oneStepRewrite[times[x,m_Integer]] := {};
oneStepRewrite[times[x,x]] := {};
oneStepRewrite[times[n_Integer,m_Integer]]:={Times[n,m]}; (*18*)
oneStepRewrite[times[e_,m_Integer]] := times[oneStepRewrite[e],m]; (*8*)
oneStepRewrite[times[n_Integer,f_]] := times[n,oneStepRewrite[f]]; (*9*)
oneStepRewrite[times[e_,f_]] := Flatten[{
    times[oneStepRewrite[e],f],
    times[e,oneStepRewrite[f]]
}]; (*8+9*)

(* Power - "pow" *)
(* Your code here... *)
oneStepRewrite[pow[e_,0]]:={1}; (*19*)
oneStepRewrite[pow[0,n_Integer?Positive]]:={0}; (*20*)
oneStepRewrite[pow[e_?Positive,f_]]:={Power[e,f]}; (*XXX*)

(* Derivation - "derivative" *)
(* Your code here... *)
oneStepRewrite[derivative[plus[e_,f_]]]:={plus[derivative[e],derivative[f]]}; (*4*)
oneStepRewrite[derivative[times[e_,f_]]]:={plus[times[e,derivative[f]],times[derivative[e],f]]}; (*5*)
oneStepRewrite[derivative[pow[e_,n_]]]:={times[derivative[e],times[n,pow[e,n-1]]]}; (*10*)

(*oneStepRewrite[times[plus[1, x], derivative[x]]]
oneStepRewrite[plus[1, x]]
oneStepRewrite[derivative[x]]

oneStepRewrite[times[derivative[plus[1, x]], x]]

oneStepRewrite[plus[times[plus[1, x], derivative[x]], times[derivative[plus[1, x]], x]]]
Clear[oneStepRewrite]*)



