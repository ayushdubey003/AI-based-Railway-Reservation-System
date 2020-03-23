import {TRAINS_LIST_LOADED,STATIONS_LIST_LOADED} from "../actionTypes";

export function loadInitialData(url,actionType,method,body={}){
    return (dispatch)=>{
        return fetch(url,{
            method: method,
        }).then(res=>{
            if(!res.ok)
                throw res.status;
            return res.json();
        }).then(res=>{
            switch(actionType){
                case STATIONS_LIST_LOADED:
                    dispatch({
                        type: actionType,
                        withError: false,
                        stationsList: res["stations"]
                    });
                    return;
                case TRAINS_LIST_LOADED:
                    dispatch({
                        type: actionType,
                        withError: false,
                        trainsList: res["trains"]
                    });
                    return;
                default:
                    dispatch({
                        type: actionType,
                        withError: false
                    })
            }
        }).catch(e=>{
            switch(actionType){
                case STATIONS_LIST_LOADED:
                    dispatch({
                        type: actionType,
                        withError: true,
                        stationsList: e
                    });
                    return;
                case TRAINS_LIST_LOADED:
                    dispatch({
                        type: actionType,
                        withError: true,
                        trainsList: e
                    });
                    return;
                default:
                    dispatch({
                        type: actionType,
                        withError: true
                    })
            }
        });
    }
}

// export default function loadStationList(stations){
//     return {
//         type: STATIONS_LIST_LOADED,
//         stationsList: stations
//     }
// }