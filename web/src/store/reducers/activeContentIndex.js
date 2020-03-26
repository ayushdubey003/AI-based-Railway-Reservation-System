export {ACTIVE_CONTENT_INDEX} from "../actionTypes";

const defaultState = {
    index: 0
}

export default function activeContentIndex(state=defaultState,action){
    switch(action.type){
        case "ACTIVE_CONTENT_INDEX":
            return {
                ...state,
                index: action.index
            }
        default:
            return state;
    }
}