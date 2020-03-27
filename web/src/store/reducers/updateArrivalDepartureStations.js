import {FROM_STATION,TO_STATION,DATE_OF_JOURNEY} from "../actionTypes";

const defaultState = {
    to_station: "",
    from_station: "",
    doj: new Date()
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
        case DATE_OF_JOURNEY:
            return{
                ...state,
                doj: action.doj
            }
        default:
            return state;
    }
}