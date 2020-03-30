import { LOAD_SRC_TO_DEST_TRAINS } from "../actionTypes";


const defaultState = {
    trains: []
}

export default function loadTrains(state=defaultState,action){
    switch(action.type){
        case LOAD_SRC_TO_DEST_TRAINS:
            return {
                ...state,
                trains: action.trains
            }
        default:
            return state;
    }
}