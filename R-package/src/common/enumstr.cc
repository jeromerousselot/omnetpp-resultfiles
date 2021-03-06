/*
 * Copyright (c) 2010, Andras Varga and Opensim Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Opensim Ltd. nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Andras Varga or Opensim Ltd. BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <stdlib.h>
#include "enumstr.h"

USING_NAMESPACE

EnumStringIterator::EnumStringIterator(const char *s)
{
     // loop through string to check its syntax
     str = s;
     current = until = -1;
     err = false;
     while ((*this)++ != -1);

     // position on first value
     if (!err) {
         str = s;
         current = until = -1;
         (*this)++;
     }
}

static void skip_whitespace(const char *&str)
{
     while (*str==' ' || *str=='\t') str++;
}

static int get_number(const char *&str, int& number)
{

     while (*str==' ' || *str=='\t') str++;
     if (*str<'0' || *str>'9')
         return 0;
     else
     {
         number = atoi(str);
         while (*str>='0' && *str<='9') str++;
         return 1;
     }
}

int EnumStringIterator::operator++(int)
{
     if (err || !str)
        return -1;

     if (current<until)
        return ++current;

     skip_whitespace(str);
     if (*str=='\0')
        return current=-1;

     if (!get_number(str,current)) {err=true;return -1;}
     until = -1;
     if (*str=='\0')
         ;
     else if (*str==',')
         str++;
     else if (*str=='-' || (*str=='.' && *(str+1)=='.'))
     {
         str++;
         if (*str=='.') str++;
         skip_whitespace(str);
         if (!get_number(str,until)) {err=true;return -1;}
         skip_whitespace(str);
         if (*str=='\0')
            ;
         else if (*str==',')
            str++;
         else
            {err=true;return -1;}
     }
     else
         {err=true;return -1;}
     return current;
}
