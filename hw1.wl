(* ::Package:: *)

isRewritable[e_]:=(Or[Head[oneStepRewrite[e]]=!=oneStepRewrite,e==={}]);

(* Number *)
oneStepRewrite[derivative[n_Integer]]:={0};

(* Symbol - "x" *)
oneStepRewrite[derivative[x]]:={1};

(* Addition - "plus" *)
(* Your code here... *)
oneStepRewrite[derivative[plus[e_,f_]]]:={plus[derivative[e],derivative[f]]};

(* Multiplication - "times" *)
(* Your code here... *)
oneStepRewrite[derivative[times[e_,f_]]]:={plus[times[e,derivative[f]],times[derivative[e],f]]};

(* Power - "pow" *)
(* Your code here... *)
oneStopRewrite[derivative[pow[e_,n_]]]:={times[derivative[e],times[n,pow[e,n-1]]]};

(* Derivation - "derivative" *)
(* Your code here... *)
