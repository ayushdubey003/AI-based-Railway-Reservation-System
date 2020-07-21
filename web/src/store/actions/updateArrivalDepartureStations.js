import {FROM_STATION,TO_STATION,DATE_OF_JOURNEY} from "../actionTypes";

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

export function updateJourneyDate(data){
    return {
        type: DATE_OF_JOURNEY,
        doj: data
    }
}