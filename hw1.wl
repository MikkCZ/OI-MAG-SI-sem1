(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0};

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1};

(* Addition - "plus" *)
(* Your code here... *)
oneStepRewrite[derivate[plus[e_,f_]]]:={plus[derivate[e],derivate[f]]};

(* Multiplication - "times" *)
(* Your code here... *)
oneStepRewrite[derivate[times[e_,f_]]]:={plus[times[e,derivate[f]],times[derivate[e],f]]};

(* Power - "pow" *)
(* Your code here... *)
oneStopRewrite[derivate[pow[e_,n_]]]:={times[derivate[e],times[n,pow[e,n-1]]]};

(* Derivation - "derivative" *)
(* Your code here... *)
