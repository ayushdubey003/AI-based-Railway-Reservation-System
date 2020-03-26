import {FROM_STATION,TO_STATION} from "../actionTypes";

const defaultState = {
    to_station: "",
    from_station: ""
}

export default function updateArrivalDepartureStations(state=defaultState,action){
    switch(action.type){
        case FROM_STATION:
            return {
                ...state,
                from_station: action.from_station
            }
        case TO_STATION:
            return {
                ...state,
                to_station: action.to_station
            }
        default:
            return state;
    }
}