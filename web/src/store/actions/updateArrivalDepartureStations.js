import {FROM_STATION,TO_STATION} from "../actionTypes";

export function updateDepartureStation(data){
    return {
        type: FROM_STATION,
        from_station: data
    }
}

export function updateArrivalStation(data){
    return {
        type: TO_STATION,
        to_station: data
    }
}