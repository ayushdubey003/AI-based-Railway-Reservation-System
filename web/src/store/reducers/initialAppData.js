import {TRAINS_LIST_LOADED,STATIONS_LIST_LOADED} from "../actionTypes";

const defaultState = {
    trainsList: [],
    stationsList: []
}

export default function initialAppData(state=defaultState,action){
    switch(action.type){
        case TRAINS_LIST_LOADED:
            return {
                ...state,
                withError: action.withError,
                trainsList: action.trainsList
            };
        case STATIONS_LIST_LOADED:
            return {
                ...state,
                stationsList: action.stationsList,
                withError: action.withError
            }
        default:
            return state;
    }
}