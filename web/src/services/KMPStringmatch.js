export default function KMPSearch(pat, txt) {
    pat=pat.toUpperCase();
    txt = txt.toUpperCase();
    let z = false;
    let M = pat.length;
    let N = txt.length;
    let lps= []
    let j = 0;
    computeLPSArray(pat, M, lps);
    let i = 0;
    while (i < N) {
        if (pat[j] == txt[i]) {
            j++;
            i++;
        }
        if (j == M) {
            z = true;
            j = lps[j - 1];
        } else if (i < N && pat[j] != txt[i]) {
            if (j != 0)
                j = lps[j - 1];
            else
                i = i + 1;
        }
    }
    return z;
}

function computeLPSArray(pat, M, lps) {
    let len = 0;
    let i = 1;
    lps.push(0); // lps[0] is always 0
    while (i < M) {
        if (pat[i] == pat[len]) {
            len++;
            lps.push(len);
            i++;
        } else {
            if (len != 0) {
                len = lps[len - 1];
            } else {
                lps.push(len);
                i++;
            }
        }
    }
}