import {LOAD_SRC_TO_DEST_TRAINS} from "../actionTypes";

export default function loadTrains(url){
    return function(dispatch){
        fetch(url).then((res)=>{
            if(!res.ok)
                throw "Error";
            return res.json();
        }).then((res)=>{
            dispatch({
                type: LOAD_SRC_TO_DEST_TRAINS,
                trains: res
            })
        }).catch((e)=>console.log(e));
    }
}